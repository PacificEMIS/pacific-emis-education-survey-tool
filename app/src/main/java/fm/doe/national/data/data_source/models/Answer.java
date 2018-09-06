package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public interface Answer {
    enum State {
        NOT_ANSWERED, POSITIVE, NEGATIVE
    }

    State getState();

    void setState(State state);

    List<SynchronizePlatform> getSynchronizedPlatforms();

    void addSynchronizedPlatform(SynchronizePlatform platform);

    @Nullable
    String getComment();

    void setComment(@NonNull String comment);

    List<String> getPhotosPaths();

    void setPhotoPath(List<String> paths);
}
