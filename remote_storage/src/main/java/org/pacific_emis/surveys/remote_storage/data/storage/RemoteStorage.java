package org.pacific_emis.surveys.remote_storage.data.storage;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Nullable;

import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.remote_storage.data.model.DriveType;
import org.pacific_emis.surveys.remote_storage.data.model.ExportType;
import org.pacific_emis.surveys.remote_storage.data.model.GoogleDriveFileHolder;
import org.pacific_emis.surveys.remote_storage.data.model.ReportBundle;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface RemoteStorage {

    Single<List<GoogleDriveFileHolder>> requestStorageFiles(String parentFolderId);

    Completable upload(Survey survey);

    Single<String> loadContent(String fileId);

    Completable delete(String fileId);

    void refreshCredentials();

    @Nullable
    GoogleSignInAccount getUserAccount();

    void setUserAccount(@Nullable GoogleSignInAccount account);

    Single<String> exportToExcel(Survey survey, ReportBundle reportBundle, ExportType exportType);

    InputStream getFileContentStream(String fileId) throws IOException;

    Completable downloadContent(String fileId, File targetFile, DriveType mimeType);
}
