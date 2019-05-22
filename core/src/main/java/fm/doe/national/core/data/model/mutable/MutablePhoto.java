package fm.doe.national.core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import fm.doe.national.core.data.model.Photo;

public class MutablePhoto extends BaseMutableEntity implements Photo {

    @Nullable
    private String localPath;

    @Nullable
    private String remotePath;

    public static MutablePhoto toMutable(@NonNull Photo photo) {
        if (photo instanceof MutablePhoto) {
            return (MutablePhoto) photo;
        }
        return new MutablePhoto(photo);
    }

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

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        MutablePhoto that = (MutablePhoto) o;
        if (localPath == null) {
            return that.getLocalPath() == null;
        }
        if (!localPath.equals(that.getLocalPath())) {
            return false;
        }
        if (remotePath == null) {
            return that.getRemotePath() == null;
        }
        return remotePath.equals(that.getRemotePath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(localPath, remotePath, getId());
    }
}
