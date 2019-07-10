package fm.doe.national.remote_storage.data.model;

public enum DriveType {
    FOLDER("application/vnd.google-apps.folder"),
    FILE("application/vnd.google-apps.file"),
    PLAIN_TEXT("text/plain"),
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
