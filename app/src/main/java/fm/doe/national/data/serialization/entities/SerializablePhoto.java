package fm.doe.national.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import fm.doe.national.data.model.Photo;

@Root(name = "photo")
public class SerializablePhoto implements Photo {

    @Nullable
    @Element(required = false)
    String localPath;

    @Nullable
    @Element(required = false)
    String remotePath;

    public SerializablePhoto() {
    }

    public SerializablePhoto(@NonNull Photo otherPhoto) {
        this.localPath = otherPhoto.getLocalPath();
        this.remotePath = otherPhoto.getRemotePath();
    }

    @Nullable
    @Override
    public String getLocalPath() {
        return localPath;
    }

    @Nullable
    @Override
    public String getRemotePath() {
        return remotePath;
    }

    @Override
    public long getId() {
        return 0;
    }
}
