package fm.doe.national.remote_storage.data.model;

import com.google.api.services.drive.model.File;

public class GoogleDriveFileHolder {

    private String id;
    private String name;
    private DriveType mimeType;

    public GoogleDriveFileHolder(File driveFile) {
        this.id = driveFile.getId();
        this.name = driveFile.getName();
        this.mimeType = DriveType.of(driveFile.getMimeType());
    }

    public GoogleDriveFileHolder() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DriveType getMimeType() {
        return mimeType;
    }
}
