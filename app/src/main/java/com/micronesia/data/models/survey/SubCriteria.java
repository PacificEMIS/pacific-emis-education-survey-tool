package com.micronesia.data.models.survey;

import android.support.annotation.NonNull;

public interface SubCriteria {

    long getId();

    long getCriteriaId();

    @NonNull
    String getName();

    @NonNull
    String getQuestion();

}
