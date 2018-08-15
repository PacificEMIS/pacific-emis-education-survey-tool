package fm.doe.national.data.data_source.models.survey;

import java.util.List;

import fm.doe.national.data.data_source.models.SynchronizePlatform;

public interface Answer {
    boolean getAnswer();
    void setAnswer(boolean answer);

    List<SynchronizePlatform> getSynchronizedPlatforms();
    void addSynchronizedPlatform(SynchronizePlatform platform);
}
