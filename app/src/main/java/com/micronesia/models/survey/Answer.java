package com.micronesia.models.survey;

import com.micronesia.models.SynchronizePlatform;

import java.io.Serializable;
import java.util.List;

public interface Answer extends Serializable {

    long getId();

    boolean getAnswer();

    void setAnswer(boolean answer);

    List<SynchronizePlatform> getSynchronizedPlatforms();

    void addSynchronizedPlatform(SynchronizePlatform platform);

}