package fm.doe.national.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fm.doe.national.data.model.Photo;

public class MutablePhoto extends BaseMutableEntity implements Photo {

    @Nullable
    private String localPath;

    @Nullable
    private String remotePath;

    public MutablePhoto() {
    }

    public MutablePhoto(@NonNull Photo otherPhoto) {
        this.id = otherPhoto.getId();
        this.localPath = otherPhoto.getLocalPath();
        this.remotePath = otherPhoto.getRemotePath();
    }

    @Nullable
    @Override
    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(@Nullable String path) {
        this.localPath = path;
    }

    @Nullable
    @Override
    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(@Nullable String remotePath) {
        this.remotePath = remotePath;
    }
}
