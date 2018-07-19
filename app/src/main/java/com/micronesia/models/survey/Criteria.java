package com.micronesia.models.survey;

import android.support.annotation.NonNull;

import java.io.Serializable;

public interface Criteria extends Serializable {

    long getId();

    long getStandardId();

    @NonNull
    String getName();

}
