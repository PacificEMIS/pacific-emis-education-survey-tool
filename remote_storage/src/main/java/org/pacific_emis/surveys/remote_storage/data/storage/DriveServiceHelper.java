package org.pacific_emis.surveys.remote_storage.data.storage;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.Nullable;

import androidx.core.util.Pair;
import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.utils.CollectionUtils;
import org.pacific_emis.surveys.core.utils.TextUtil;
import org.pacific_emis.surveys.remote_storage.data.model.DriveType;
import org.pacific_emis.surveys.remote_storage.data.model.GoogleDriveFileHolder;
import org.pacific_emis.surveys.remote_storage.data.model.PhotoMetadata;
import org.pacific_emis.surveys.remote_storage.data.model.SurveyMetadata;
import org.pacific_emis.surveys.remote_storage.utils.DriveQueryBuilder;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class DriveServiceHelper extends TasksRxWrapper {

    private static final String FOLDER_ROOT = "root";
    private static final String SPACE_DRIVE = "drive";
    private static final String ORDER_BY_CREATED_TIME = "createdTime desc";
    private static final String FIELDS_TO_QUERY = "files(id, name, mimeType, properties)";
    private static final String FIELD_ID = "id";
    private static final String FOLDERNAME_PHOTOS = "photos";
    private static final String MIME_TYPE_JPEG = "image/jpeg";

    private final Drive drive;
    private final Executor executor = Executors.newCachedThreadPool();

    public DriveServiceHelper(Drive driveService) {
        drive = driveService;
    }

    public Single<String> createOrUpdateFile(final String fileName,
                                             final String content,
                                             final SurveyMetadata surveyMetadata,
                                             @Nullable final String folderId) {
        ByteArrayContent contentStream = ByteArrayContent.fromString(DriveType.XML.getValue(), content);
        List<String> root = Collections.singletonList(folderId == null ? FOLDER_ROOT : folderId);
        String query = new DriveQueryBuilder()
                .parentId(root.get(0))
                .mimeType(DriveType.XML.getValue())
                .name(fileName)
                .build();

        return requestFiles(query)
                .flatMap(existingFiles -> {
                    if (!existingFiles.getFiles().isEmpty()) {
                        File existingFile = existingFiles.getFiles().get(0);
                        String fileId = existingFile.getId();

                        return wrapWithSingleInThreadPool(
                                () -> {
                                    drive.files()
                                            .update(fileId, surveyMetadata.applyToDriveFile(existingFile), contentStream)
                                            .execute();
                                    return fileId;
                                },
                                fileId
                        );
                    }

                    File metadata = surveyMetadata.applyToDriveFile(
                            new File()
                                    .setParents(root)
                                    .setMimeType(DriveType.XML.getValue())
                                    .setName(fileName)
                    );

                    return wrapWithSingleInThreadPool(() -> drive.files().create(metadata, contentStream).execute().getId(), "");
                });
    }

    public Single<String> createFolderIfNotExist(final String folderName, @Nullable final String parentFolderId) {
        List<String> root = Collections.singletonList(parentFolderId == null ? FOLDER_ROOT : parentFolderId);

        String query = new DriveQueryBuilder()
                .parentId(root.get(0))
                .mimeType(DriveType.FOLDER.getValue())
                .name(folderName)
                .build();

        return requestFiles(query, true)
                .flatMap(fileList -> {
                    List<File> foundedFiles = fileList.getFiles();
                    if (!CollectionUtils.isEmpty(foundedFiles)) {
                        return Single.just(foundedFiles.get(0).getId());
                    }

                    File file = new File()
                            .setParents(root)
                            .setMimeType(DriveType.FOLDER.getValue())
                            .setName(folderName);

                    return wrapWithSingleInThreadPool(() -> drive.files().create(file).execute().getId(), "")
                            // Double check for duplicate folder
                            .flatMap(createdFolderId -> requestFiles(query, true)
                                    .flatMap(fileList2 -> {

                                        List<File> foundedFiles2 = fileList2.getFiles();

                                        if (foundedFiles2.isEmpty()) {
                                            return Single.just(createdFolderId);

                                        }

                                        Collections.reverse(foundedFiles2);

                                        String firstFolderId = foundedFiles2.get(0).getId();

                                        if (!firstFolderId.equals(createdFolderId)) {
                                            return delete(createdFolderId)
                                                    .andThen(Single.just(firstFolderId));
                                        } else {
                                            return Single.just(firstFolderId);

                                        }
                                    }));
                });
    }

    public InputStream getFileContentStream(String fileId) throws IOException {
        return drive.files().get(fileId).executeMediaAsInputStream();
    }

    public Single<String> readFile(final String fileId) {
        return Single.fromCallable(() -> {
            try (InputStream is = getFileContentStream(fileId);
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
        String parent = folderId == null ? FOLDER_ROOT : folderId;

        String query = new DriveQueryBuilder()
                .parentId(parent)
                .build();

        return requestFiles(query)
                .flatMap(fileList -> Single.fromCallable(() -> {
                    List<File> files = fileList.getFiles();
                    List<GoogleDriveFileHolder> googleDriveFileHolderList = new ArrayList<>();

                    if (CollectionUtils.isEmpty(files)) {
                        return googleDriveFileHolderList;
                    }

                    for (File file : files) {
                        googleDriveFileHolderList.add(new GoogleDriveFileHolder(file));
                    }

                    return googleDriveFileHolderList;
                }));
    }

    public Completable delete(String fileId) {
        return wrapWithCompletableInThreadPool(() -> {
            if (fileId != null) {
                drive.files().delete(fileId).execute();
            }
        });
    }

    private Single<FileList> requestFiles(String query) {
        return requestFiles(query, false);
    }

    private Single<FileList> requestFiles(String query, boolean orderByCreateTime) {
        return wrapWithSingleInThreadPool(
                () -> drive.files().list()
                        .setQ(query)
                        .setFields(FIELDS_TO_QUERY)
                        .setSpaces(SPACE_DRIVE)
                        .setOrderBy(orderByCreateTime ? ORDER_BY_CREATED_TIME : null)
                        .execute(),
                new FileList()
        );
    }

    public Single<File> uploadFileFromSource(Drive service,
                                             java.io.File source,
                                             String sourceMimeType,
                                             String targetMimeType,
                                             String targetName) {
        return Single.fromCallable(() -> {
            File fileMetadata = new File()
                    .setName(targetName)
                    .setMimeType(targetMimeType);

            FileContent mediaContent = new FileContent(sourceMimeType, source);
            return service.files().create(fileMetadata, mediaContent)
                    .setFields(FIELD_ID)
                    .execute();
        })
                .subscribeOn(Schedulers.io());
    }

    public Single<List<Pair<Photo, File>>> uploadPhotos(List<Photo> photos, String parentFolderId, PhotoMetadata photoMetadata) {
        return createFolderIfNotExist(FOLDERNAME_PHOTOS, parentFolderId)
                .flatMapObservable(photosFolderId -> Observable.fromIterable(photos)
                        .concatMapSingle(photo -> {
                            List<String> root = Collections.singletonList(photosFolderId);

                            java.io.File photoFile = new java.io.File(photo.getLocalPath());

                            if (!photoFile.exists()) {
                                return Single.just(Pair.create(photo, (File) null));
                            }

                            String fileName = TextUtil.getFileNameWithoutExtension(photo.getLocalPath());
                            String query = new DriveQueryBuilder()
                                    .parentId(root.get(0))
                                    .mimeType(MIME_TYPE_JPEG)
                                    .name(fileName)
                                    .build();

                            return requestFiles(query)
                                    .flatMap(fileList -> {
                                        if (!CollectionUtils.isEmpty(fileList.getFiles())) {
                                            return Single.just(Pair.create(photo, (File) null));
                                        }

                                        return wrapWithSingleInThreadPool(() -> {
                                            FileContent mediaContent = new FileContent(MIME_TYPE_JPEG, photoFile);
                                            File metadata = photoMetadata.applyToDriveFile(
                                                    new File()
                                                            .setParents(root)
                                                            .setMimeType(MIME_TYPE_JPEG)
                                                            .setName(fileName)
                                            );
                                            return Pair.create(
                                                    photo,
                                                    drive.files()
                                                            .create(metadata, mediaContent)
                                                            .execute()
                                            );
                                        }, Pair.create(photo, (File) null));
                                    });
                        }))
                .toList();
    }

    public Single<Optional<String>> getFileId(final String name, @Nullable final String parentFolderId) {
        List<String> root = Collections.singletonList(parentFolderId == null ? FOLDER_ROOT : parentFolderId);

        String query = new DriveQueryBuilder()
                .parentId(root.get(0))
                .name(name)
                .build();

        return requestFiles(query)
                .flatMap(fileList -> {
                    List<File> foundedFiles = fileList.getFiles();

                    if (!CollectionUtils.isEmpty(foundedFiles)) {
                        return Single.just(Optional.of(foundedFiles.get(0).getId()));
                    }

                    return Single.just(Optional.empty());
                });
    }

    public Completable downloadContent(String fileId, java.io.File targetFile, DriveType mimeType) {
        return wrapWithCompletableInThreadPool(() -> {
            OutputStream outputStream = new FileOutputStream(targetFile);
            drive.files()
                    .export(fileId, mimeType.getValue())
                    .executeMediaAndDownloadTo(outputStream);
        });
    }
}
