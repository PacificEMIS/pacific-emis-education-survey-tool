package com.micronesia.models.survey;

import android.support.annotation.NonNull;

import java.io.Serializable;

public interface SubCriteria extends Serializable {

    long getId();

    long getCriteriaId();

    @NonNull
    String getName();

    @NonNull
    String getQuestion();

}
