package fm.doe.national.remote_storage.utils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fm.doe.national.core.data.exceptions.FileExportException;
import fm.doe.national.core.data.files.FilesRepository;
import fm.doe.national.remote_storage.BuildConfig;
import fm.doe.national.remote_storage.data.model.DriveType;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import io.reactivex.Single;

public class RemoteStorageUtils {

    private static final Pattern sGoogleSpreadsheetIdPattern = Pattern.compile("/spreadsheets/d/([a-zA-Z0-9-_]+)");

    public static Single<String> downloadReport(RemoteStorage remoteStorage,
                                                FilesRepository filesRepository,
                                                String fileId) {

        return Single.fromCallable(() -> {
            File downloadFile = filesRepository.createFile(fileId, "." + BuildConfig.EXTENSION_REPORT_TEMPLATE);

            if (!downloadFile.exists()) {
                throw new FileExportException("Failed to create file");
            }

            return downloadFile;
        })
                .flatMap(downloadFile -> remoteStorage.downloadContent(fileId, downloadFile, DriveType.EXCEL)
                        .andThen(Single.just(downloadFile.getAbsolutePath())));
    }

    public static Single<String> downloadReportFromUrl(RemoteStorage remoteStorage,
                                                       FilesRepository filesRepository,
                                                       String url) {
        Matcher matcher = sGoogleSpreadsheetIdPattern.matcher(url);

        if (matcher.find()) {
            return downloadReport(remoteStorage, filesRepository, matcher.group(1));
        }

        return Single.error(new FileExportException("Failed to find fileId"));
    }

}
