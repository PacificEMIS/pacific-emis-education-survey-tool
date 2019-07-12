package fm.doe.national.remote_storage.data.model;

import com.google.api.services.drive.model.File;

public class GoogleDriveFileHolder {

    private final String id;
    private final String name;
    private final DriveType mimeType;
    private final NdoeMetadata ndoeMetadata;

    public GoogleDriveFileHolder(File driveFile) {
        this.id = driveFile.getId();
        this.name = driveFile.getName();
        this.mimeType = DriveType.of(driveFile.getMimeType());
        this.ndoeMetadata = NdoeMetadata.extract(driveFile);
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

    public NdoeMetadata getNdoeMetadata() {
        return ndoeMetadata;
    }
}
