package fm.doe.national.data.data_source.models;

import java.util.List;

public interface Answer {
    enum State {
        NOT_ANSWERED, POSITIVE, NEGATIVE
    }

    State getState();

    void setState(State state);

    List<SynchronizePlatform> getSynchronizedPlatforms();

    void addSynchronizedPlatform(SynchronizePlatform platform);
}
