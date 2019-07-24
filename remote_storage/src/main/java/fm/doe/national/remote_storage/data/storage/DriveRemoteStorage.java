package fm.doe.national.remote_storage.data.storage;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.omega_r.libs.omegatypes.Text;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import fm.doe.national.core.data.exceptions.FileExportException;
import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.data.files.FilesRepository;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.SurveySerializer;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.remote_storage.BuildConfig;
import fm.doe.national.remote_storage.R;
import fm.doe.national.remote_storage.data.export.FcmSheetsExcelExporter;
import fm.doe.national.remote_storage.data.export.RmiSheetsExcelExporter;
import fm.doe.national.remote_storage.data.export.SheetsExcelExporter;
import fm.doe.national.remote_storage.data.model.ExportType;
import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;
import fm.doe.national.remote_storage.data.model.NdoeMetadata;
import fm.doe.national.remote_storage.data.model.ReportBundle;
import fm.doe.national.remote_storage.utils.SurveyTextUtil;
import io.reactivex.Completable;
import io.reactivex.Single;

public final class DriveRemoteStorage implements RemoteStorage {

    private static final Collection<String> sScopes = Arrays.asList(
            DriveScopes.DRIVE_FILE,
            DriveScopes.DRIVE_METADATA,
            SheetsScopes.SPREADSHEETS,
            SheetsScopes.SPREADSHEETS_READONLY
    );
    private static final String SHEET_NAME_SUMMARY = "Standard Scores Summary";
    private static final String SHEET_NAME_TEMPLATE = "template";
    private static final HttpTransport sTransport = AndroidHttp.newCompatibleTransport();
    private static final GsonFactory sGsonFactory = new GsonFactory();
    private final SurveySerializer surveySerializer;
    private final FilesRepository filesRepository;

    private final Context appContext;
    private final GlobalPreferences globalPreferences;

    private DriveServiceHelper driveServiceHelper;

    @Nullable
    private GoogleSignInAccount userAccount;

    private GoogleCredential serviceCredentials;

    public DriveRemoteStorage(Context appContext,
                              GlobalPreferences globalPreferences,
                              SurveySerializer surveySerializer,
                              FilesRepository filesRepository) {
        this.appContext = appContext;
        this.globalPreferences = globalPreferences;
        this.surveySerializer = surveySerializer;
        this.filesRepository = filesRepository;
        refreshCredentials();
        userAccount = GoogleSignIn.getLastSignedInAccount(appContext);
    }

    @Override
    public void refreshCredentials() {
        try {
            serviceCredentials = GoogleCredential.fromStream(
                    appContext.getAssets().open(getCredentialsFileName()),
                    sTransport,
                    sGsonFactory)
                    .createScoped(sScopes);
            Drive drive = getDriveService(serviceCredentials);
            driveServiceHelper = new DriveServiceHelper(drive);
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
        switch (globalPreferences.getAppRegion()) {
            case FCM:
                return new FcmSheetsExcelExporter(appContext, sheets);
            case RMI:
                return new RmiSheetsExcelExporter(appContext, sheets);
            default:
                throw new NotImplementedException();
        }
    }

    private String getCredentialsFileName() {
        switch (globalPreferences.getOperatingMode()) {
            case DEV:
                return BuildConfig.CREDENTIALS_DEV;
            case PROD:
                return BuildConfig.CREDENTIALS_PROD;
            default:
                return BuildConfig.CREDENTIALS_DEV;
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

        return driveServiceHelper.createFolderIfNotExist(unwrap(survey.getAppRegion().getName()), null)
                .flatMap(regionFolderId -> driveServiceHelper.createOrUpdateFile(
                        SurveyTextUtil.createSurveyFileName(survey),
                        surveySerializer.serialize(survey),
                        new NdoeMetadata(survey, userAccount.getEmail()),
                        regionFolderId))
                .ignoreElement();
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
                fileIdStep = Single.just(globalPreferences.getSpreadsheetId());
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

    private Single<String> copyTemplateReportToUserDrive(HttpRequestInitializer initializer) {
        Drive drive = getDriveService(initializer);
        String templateName = getTemplateFileName();
        String templateExtension = BuildConfig.EXTENSION_REPORT_TEMPLATE;
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
                                SheetsExcelExporter.MIME_TYPE_MS_EXCEL,
                                SheetsExcelExporter.MIME_TYPE_GOOGLE_SHEETS,
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
        switch (globalPreferences.getAppRegion()) {
            case FCM:
                return BuildConfig.NAME_REPORT_TEMPLATE_FCM;
            case RMI:
                return BuildConfig.NAME_REPORT_TEMPLATE_RMI;
            default:
                throw new NotImplementedException();
        }
    }

}
