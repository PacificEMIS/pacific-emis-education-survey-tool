package fm.doe.national.data.models.survey;

import java.util.List;

import fm.doe.national.data.models.SynchronizePlatform;

public interface Answer {
    boolean getAnswer();
    void setAnswer(boolean answer);

    List<SynchronizePlatform> getSynchronizedPlatforms();
    void addSynchronizedPlatform(SynchronizePlatform platform);
}
