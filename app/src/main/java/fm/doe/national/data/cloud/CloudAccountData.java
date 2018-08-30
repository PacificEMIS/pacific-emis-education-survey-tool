package fm.doe.national.data.cloud;

public class CloudAccountData {
    private CloudType type;
    private String email;
    private String exportPath;
    private boolean isDefault;

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
