package com.micronesia.data.models.survey;

import com.micronesia.data.models.SynchronizePlatform;

import java.util.List;

public interface Answer {

    long getId();

    long getSurveyId();

    long getSubCriteriaId();

    boolean getAnswer();

    void setAnswer(boolean answer);

    List<SynchronizePlatform> getSynchronizedPlatforms();

    void addSynchronizedPlatform(SynchronizePlatform platform);

}