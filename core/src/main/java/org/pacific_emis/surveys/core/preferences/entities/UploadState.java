package org.pacific_emis.surveys.core.preferences.entities;

public enum UploadState {
    NOT_UPLOAD,
    IN_PROGRESS,
    SUCCESSFULLY;

    public static UploadState getOrDefault(String value) {
        try {
            return valueOf(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            return UploadState.NOT_UPLOAD;
        }
    }

}
