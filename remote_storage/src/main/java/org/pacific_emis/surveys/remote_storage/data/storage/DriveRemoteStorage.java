package org.pacific_emis.surveys.remote_storage.data.storage;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.core.data.exceptions.FileExportException;
import org.pacific_emis.surveys.core.data.exceptions.NotImplementedException;
import org.pacific_emis.surveys.core.data.files.FilesRepository;
import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.data_source_injector.di.DataSourceComponent;
import org.pacific_emis.surveys.remote_storage.BuildConfig;
import org.pacific_emis.surveys.remote_storage.R;
import org.pacific_emis.surveys.remote_storage.data.export.FsmSheetsExcelExporter;
import org.pacific_emis.surveys.remote_storage.data.export.RmiSheetsExcelExporter;
import org.pacific_emis.surveys.remote_storage.data.export.SheetsExcelExporter;
import org.pacific_emis.surveys.remote_storage.data.model.DriveType;
import org.pacific_emis.surveys.remote_storage.data.model.ExportType;
import org.pacific_emis.surveys.remote_storage.data.model.GoogleDriveFileHolder;
import org.pacific_emis.surveys.remote_storage.data.model.PhotoMetadata;
import org.pacific_emis.surveys.remote_storage.data.model.ReportBundle;
import org.pacific_emis.surveys.remote_storage.data.model.SurveyMetadata;
import org.pacific_emis.surveys.remote_storage.utils.SurveyTextUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public final class DriveRemoteStorage implements RemoteStorage {

    private static final Collection<String> sScopes = Arrays.asList(
            DriveScopes.DRIVE_FILE,
            DriveScopes.DRIVE_METADATA,
            SheetsScopes.SPREADSHEETS,
            SheetsScopes.SPREADSHEETS_READONLY
    );
    private static final String SHEET_NAME_SUMMARY = "Standard Scores Summary";
    private static final String SHEET_NAME_TEMPLATE = "template";
    private static final HttpTransport sTransport = new NetHttpTransport();
    private static final GsonFactory sGsonFactory = new GsonFactory();
    private final FilesRepository filesRepository;
    private final DataSourceComponent dataSourceComponent;

    private final Context appContext;
    private final LocalSettings localSettings;

    private DriveServiceHelper driveServiceHelper;

    @Nullable
    private GoogleSignInAccount userAccount;

    private GoogleCredential serviceCredentials;

    public DriveRemoteStorage(Context appContext,
                              LocalSettings localSettings,
                              DataSourceComponent dataSourceComponent,
                              FilesRepository filesRepository) {
        this.appContext = appContext;
        this.localSettings = localSettings;
        this.dataSourceComponent = dataSourceComponent;
        this.filesRepository = filesRepository;
        refreshCredentials();
        userAccount = GoogleSignIn.getLastSignedInAccount(appContext);
    }

    @Override
    public void refreshCredentials() {
        try {
            InputStream credentialsStream = getCredentialsStream();

            if (credentialsStream != null) {
                serviceCredentials = GoogleCredential.fromStream(
                        credentialsStream,
                        sTransport,
                        sGsonFactory)
                        .createScoped(sScopes);
                Drive drive = getDriveService(serviceCredentials);
                driveServiceHelper = new DriveServiceHelper(drive);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Drive getDriveService(HttpRequestInitializer initializer) {
        return new Drive.Builder(sTransport, sGsonFactory, initializer)
                .setApplicationName(appContext.getString(R.string.app_name))
                .build();
    }

    private SheetsExcelExporter getExcelExporter(HttpRequestInitializer initializer) {
        Sheets sheets = new Sheets.Builder(sTransport, sGsonFactory, initializer)
                .setApplicationName(appContext.getString(R.string.app_name))
                .build();
        switch (localSettings.getCurrentAppRegion()) {
            case FSM:
                return new FsmSheetsExcelExporter(appContext, sheets);
            case RMI:
                return new RmiSheetsExcelExporter(appContext, sheets);
            default:
                throw new NotImplementedException();
        }
    }

    @Nullable
    private InputStream getCredentialsStream() throws IOException {
        switch (localSettings.getOperatingMode()) {
            case DEV:
                return appContext.getAssets().open(BuildConfig.CREDENTIALS_DEV);
            case PROD:
                String cert = localSettings.getProdCert();

                if (cert == null) {
                    return null;
                }

                return new ByteArrayInputStream(cert.getBytes());
            default:
                return null;
        }
    }

    @Override
    public Single<List<GoogleDriveFileHolder>> requestStorageFiles(String parentFolderId) {
        return driveServiceHelper.queryFiles(parentFolderId);
    }

    @Override
    public Completable upload(Survey survey) {
        if (userAccount == null) {
            return Completable.error(new IllegalStateException());
        }

        String creator = survey.getCreateUser();
        return driveServiceHelper.createFolderIfNotExist(unwrap(survey.getAppRegion().getName()), null)
                .flatMapCompletable(regionFolderId -> {
                    List<Photo> photos = dataSourceComponent.getDataRepository().getPhotos(survey);
                    return driveServiceHelper.uploadPhotos(photos, regionFolderId, new PhotoMetadata(survey))
                            .flatMapObservable(Observable::fromIterable)
                            .filter(photoFilePair -> photoFilePair.second != null)
                            .concatMapCompletable(photoFilePair -> dataSourceComponent.getDataRepository()
                                    .updatePhotoWithRemote(
                                            photoFilePair.first,
                                            photoFilePair.second.getId()
                                    )
                                    .subscribeOn(Schedulers.io())
                            )
                            .andThen(dataSourceComponent.getDataRepository().loadSurvey(survey.getAppRegion(), survey.getId())
                                    .subscribeOn(Schedulers.io()))
                            .flatMapCompletable(updatedSurvey -> driveServiceHelper.createOrUpdateFile(
                                    SurveyTextUtil.createSurveyFileName(updatedSurvey, creator),
                                    dataSourceComponent.getSurveySerializer().serialize(updatedSurvey),
                                    new SurveyMetadata(updatedSurvey, creator),
                                    regionFolderId)
                                    .ignoreElement()
                            );
                });
    }

    @Override
    public Single<String> loadContent(String fileId) {
        return driveServiceHelper.readFile(fileId);
    }

    private String unwrap(@NonNull Text text) {
        return text.getString(appContext);
    }

    @Override
    public Completable delete(String fileId) {
        return driveServiceHelper.delete(fileId);
    }

    @Nullable
    @Override
    public GoogleSignInAccount getUserAccount() {
        return userAccount;
    }

    @Override
    public void setUserAccount(@Nullable GoogleSignInAccount account) {
        this.userAccount = account;
    }

    @Override
    public Single<String> exportToExcel(Survey survey, ReportBundle reportBundle, ExportType exportType) {
        Single<String> fileIdStep;
        SheetsExcelExporter excelExporter;
        switch (exportType) {
            case GLOBAL:
                fileIdStep = copyTemplateReportToServiceDriveIfNotExist();
                excelExporter = getExcelExporter(serviceCredentials);
                break;
            case PRIVATE:
                GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(appContext, sScopes);
                credential.setSelectedAccount(userAccount.getAccount());
                excelExporter = getExcelExporter(credential);
                fileIdStep = copyTemplateReportToUserDrive(credential);
                break;
            default:
                throw new NotImplementedException();
        }

        String sheetName = SurveyTextUtil.createSurveySheetName(survey);
        return fileIdStep
                .flatMap(spreadsheetId ->
                        excelExporter.recreateSheet(spreadsheetId, sheetName, SHEET_NAME_TEMPLATE)
                                .flatMap(url ->
                                        excelExporter.fillReportSheet(spreadsheetId, sheetName, reportBundle)
                                                .andThen(excelExporter.updateSummarySheet(spreadsheetId, SHEET_NAME_SUMMARY, reportBundle))
                                                .andThen(Single.just(url))
                                )
                );
    }

    private Single<String> copyTemplateReportToServiceDriveIfNotExist() {
        Drive drive = getDriveService(serviceCredentials);
        String templateName = getTemplateFileName();
        String templateExtension = BuildConfig.EXTENSION_REPORT_TEMPLATE;
        return driveServiceHelper.getFileId(templateName, null)
                .flatMap(optionalFileId -> {
                    if (optionalFileId.isPresent()) {
                        return Single.just(optionalFileId.get());
                    }

                    return uploadExcelTemplate(drive, templateName, templateExtension);
                });
    }

    private Single<String> copyTemplateReportToUserDrive(HttpRequestInitializer initializer) {
        Drive drive = getDriveService(initializer);
        String templateName = getTemplateFileName();
        String templateExtension = BuildConfig.EXTENSION_REPORT_TEMPLATE;
        return uploadExcelTemplate(drive, templateName, templateExtension);
    }

    private Single<String> uploadExcelTemplate(Drive drive,
                                               String templateName,
                                               String templateExtension) {
        return Single.fromCallable(() ->
                filesRepository.createTmpFile(
                        templateName,
                        templateExtension,
                        appContext.getAssets().open(templateName + "." + templateExtension)
                )
        )
                .flatMap(file ->
                        driveServiceHelper.uploadFileFromSource(
                                drive,
                                file,
                                DriveType.EXCEL.getValue(),
                                DriveType.GOOGLE_SHEETS.getValue(),
                                templateName
                        )
                )
                .flatMap(file -> {
                    String fileId = file.getId();
                    if (TextUtils.isEmpty(fileId)) {
                        return Single.error(new FileExportException("File not created"));
                    } else {
                        return Single.just(fileId);
                    }
                });
    }

    private String getTemplateFileName() {
        switch (localSettings.getCurrentAppRegion()) {
            case FSM:
                return BuildConfig.NAME_REPORT_TEMPLATE_FSM;
            case RMI:
                return BuildConfig.NAME_REPORT_TEMPLATE_RMI;
            default:
                throw new NotImplementedException();
        }
    }

    @Override
    public InputStream getFileContentStream(String fileId) throws IOException {
        return driveServiceHelper.getFileContentStream(fileId);
    }

    @Override
    public Completable downloadContent(String fileId, File targetFile, DriveType mimeType) {
        return driveServiceHelper.downloadContent(fileId, targetFile, mimeType);
    }
}
