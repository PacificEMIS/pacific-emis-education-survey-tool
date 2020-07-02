package fm.doe.national.remote_storage.data.model;

import com.google.api.services.drive.model.File;

public class GoogleDriveFileHolder {

    private final String id;
    private final String name;
    private final DriveType mimeType;
    private final SurveyMetadata surveyMetadata;

    public GoogleDriveFileHolder(File driveFile) {
        this.id = driveFile.getId();
        this.name = driveFile.getName();
        this.mimeType = DriveType.of(driveFile.getMimeType());
        this.surveyMetadata = SurveyMetadata.extract(driveFile);
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

    public SurveyMetadata getSurveyMetadata() {
        return surveyMetadata;
    }
}
