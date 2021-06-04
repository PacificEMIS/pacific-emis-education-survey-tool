package org.pacific_emis.surveys.remote_storage.data.model;

public enum DriveType {
    FOLDER("application/vnd.google-apps.folder"),
    FILE("application/vnd.google-apps.file"),
    XML("text/xml"),
    GOOGLE_SHEETS("application/vnd.google-apps.spreadsheet"),
    EXCEL("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    OTHER("");

    public static DriveType of(String mimeType) {
        for (DriveType driveType : DriveType.values()) {
            if (driveType.value.equals(mimeType)) {
                return driveType;
            }
        }
        return DriveType.OTHER;
    }

    private final String value;

    DriveType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
