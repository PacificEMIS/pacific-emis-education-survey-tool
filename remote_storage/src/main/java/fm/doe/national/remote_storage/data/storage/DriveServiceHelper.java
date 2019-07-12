package fm.doe.national.remote_storage.data.storage;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fm.doe.national.remote_storage.data.model.DriveType;
import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;
import fm.doe.national.remote_storage.utils.DriveQueryBuilder;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class DriveServiceHelper {

    private static final String FOLDER_ROOT = "root";
    private static final String SPACE_DRIVE = "drive";

    private final Drive drive;

    public DriveServiceHelper(Drive driveService) {
        drive = driveService;
    }

    public Single<String> createOrUpdateFile(final String fileName,
                                             final String content,
                                             @Nullable final String folderId) {
        return Single.fromCallable(() -> {
            ByteArrayContent contentStream = ByteArrayContent.fromString(DriveType.PLAIN_TEXT.getValue(), content);
            List<String> root;

            if (folderId == null) {
                root = Collections.singletonList(FOLDER_ROOT);
            } else {
                root = Collections.singletonList(folderId);
            }

            String query = new DriveQueryBuilder()
                    .parentId(root.get(0))
                    .mimeType(DriveType.PLAIN_TEXT.getValue())
                    .name(fileName)
                    .build();
            FileList existingFiles = drive.files()
                    .list()
                    .setQ(query)
                    .setSpaces(SPACE_DRIVE)
                    .execute();

            if (!existingFiles.getFiles().isEmpty()) {
                String fileId = existingFiles.getFiles().get(0).getId();
                drive.files().update(fileId, new File().setName(fileName), contentStream).execute();
                return fileId;
            }

            File metadata = new File()
                    .setParents(root)
                    .setMimeType(DriveType.PLAIN_TEXT.getValue())
                    .setName(fileName);
            File googleFile = drive.files().create(metadata, contentStream).execute();

            if (googleFile == null) {
                throw new IOException("Null result when requesting file creation.");
            }

            return googleFile.getId();
        }).subscribeOn(Schedulers.io());
    }

    public Single<String> createFolderIfNotExist(final String folderName, @Nullable final String parentFolderId) {
        return Single.fromCallable(() -> {
            List<String> root = Collections.singletonList(parentFolderId == null ? FOLDER_ROOT : parentFolderId);

            String query = new DriveQueryBuilder()
                    .parentId(root.get(0))
                    .mimeType(DriveType.FOLDER.getValue())
                    .name(folderName)
                    .build();
            FileList result = drive.files().list()
                    .setQ(query)
                    .setSpaces(SPACE_DRIVE)
                    .execute();

            if (!result.getFiles().isEmpty()) {
                return result.getFiles().get(0).getId();
            }

            File metadata = new File()
                    .setParents(root)
                    .setMimeType(DriveType.FOLDER.getValue())
                    .setName(folderName);
            File googleFile = drive.files().create(metadata).execute();

            if (googleFile == null) {
                throw new IOException("Null result when requesting file creation.");
            }

            return googleFile.getId();
        }).subscribeOn(Schedulers.io());
    }

    public Single<String> readFile(final String fileId) {
        return Single.fromCallable(() -> {
            try (InputStream is = drive.files().get(fileId).executeMediaAsInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                return stringBuilder.toString();
            }
        }).subscribeOn(Schedulers.io());
    }

    public Single<List<GoogleDriveFileHolder>> queryFiles(@Nullable final String folderId) {
        return Single.fromCallable(() -> {
            List<GoogleDriveFileHolder> googleDriveFileHolderList = new ArrayList<>();
            String parent = folderId == null ? FOLDER_ROOT : folderId;

            String query = new DriveQueryBuilder()
                    .parentId(parent)
                    .build();
            FileList result = drive.files().list()
                    .setQ(query)
                    .setFields("files(id, name, size, createdTime, modifiedTime, starred, mimeType)")
                    .setSpaces(SPACE_DRIVE)
                    .execute();

            for (File file : result.getFiles()) {
                googleDriveFileHolderList.add(new GoogleDriveFileHolder(file));
            }

            return googleDriveFileHolderList;
        }).subscribeOn(Schedulers.io());
    }

    public Completable delete(String fileId) {
        return Completable.fromAction(() -> drive.files().delete(fileId).execute());
    }
}
