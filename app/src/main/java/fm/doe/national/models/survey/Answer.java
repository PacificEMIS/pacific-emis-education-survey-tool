package fm.doe.national.models.survey;

import java.io.Serializable;
import java.util.List;

import fm.doe.national.models.SynchronizePlatform;

public interface Answer extends Serializable {

    long getId();

    boolean getAnswer();

    void setAnswer(boolean answer);

    List<SynchronizePlatform> getSynchronizedPlatforms();

    void addSynchronizedPlatform(SynchronizePlatform platform);

}