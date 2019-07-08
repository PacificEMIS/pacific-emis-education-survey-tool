package fm.doe.national.remote_storage.data.accessor;
/**
 * Copyright 2018 Google LLC
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


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

import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;
import fm.doe.national.remote_storage.utils.DriveQueryBuilder;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


public class DriveServiceHelper {

    public static String TYPE_GOOGLE_DRIVE_FILE = "application/vnd.google-apps.file";
    public static String TYPE_GOOGLE_DRIVE_FOLDER = "application/vnd.google-apps.folder";
    public static String TYPE_PLAIN_TEXT = "text/plain";
    private static String FOLDER_ROOT = "root";
    private static String SPACE_DRIVE = "drive";

    private final Drive drive;

    public DriveServiceHelper(Drive driveService) {
        drive = driveService;
    }

    public Single<String> createOrUpdateFile(final String fileName,
                                             final String content,
                                             @Nullable final String folderId) {
        return Single.fromCallable(() -> {
            ByteArrayContent contentStream = ByteArrayContent.fromString(TYPE_PLAIN_TEXT, content);
            List<String> root;

            if (folderId == null) {
                root = Collections.singletonList(FOLDER_ROOT);
            } else {
                root = Collections.singletonList(folderId);
            }

            String query = new DriveQueryBuilder()
                    .parentId(root.get(0))
                    .mimeType(TYPE_PLAIN_TEXT)
                    .name(fileName)
                    .build();
            FileList existingFiles = drive.files().list()
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
                    .setMimeType(TYPE_PLAIN_TEXT)
                    .setName(fileName);
            File googleFile = drive.files().create(metadata, contentStream).execute();

            if (googleFile == null) {
                throw new IOException("Null result when requesting file creation.");
            }

            return googleFile.getId();
        }).subscribeOn(Schedulers.io());
    }

    public Single<String> createFolderIfNotExist(final String folderName, @Nullable final String folderId) {
        return Single.fromCallable(() -> {
            List<String> root = Collections.singletonList(folderId == null ? FOLDER_ROOT : folderId);

            String query = new DriveQueryBuilder()
                    .parentId(root.get(0))
                    .mimeType(TYPE_GOOGLE_DRIVE_FOLDER)
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
                    .setMimeType(TYPE_GOOGLE_DRIVE_FOLDER)
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
                GoogleDriveFileHolder googleDriveFileHolder = new GoogleDriveFileHolder();
                googleDriveFileHolder.setId(file.getId());
                googleDriveFileHolder.setName(file.getName());
                googleDriveFileHolder.setMimeType(file.getMimeType());
                googleDriveFileHolderList.add(googleDriveFileHolder);
            }

            return googleDriveFileHolderList;
        }).subscribeOn(Schedulers.io());
    }
}
