package fm.doe.national.data.serialization.entities;

import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

import fm.doe.national.data.model.Photo;

@Root(name = "photo")
public class SerializablePhoto implements Photo, Serializable {

    @Nullable
    @Element(required = false)
    String localPath;

    @Nullable
    @Element(required = false)
    String remotePath;

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
