package org.pacific_emis.surveys.remote_storage.data.uploader;

public interface RemoteUploader {
    void scheduleUploading(long passingId);
}
