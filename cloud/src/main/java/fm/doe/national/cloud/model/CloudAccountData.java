package fm.doe.national.cloud.model;

public class CloudAccountData {
    private final CloudType type;
    private final String email;
    private final String exportPath;
    private final boolean isDefault;

    public CloudAccountData(CloudType type, String email, String exportPath, boolean isDefault) {
        this.type = type;
        this.email = email;
        this.exportPath = exportPath;
        this.isDefault = isDefault;
    }

    public CloudType getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public String getExportPath() {
        return exportPath;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
