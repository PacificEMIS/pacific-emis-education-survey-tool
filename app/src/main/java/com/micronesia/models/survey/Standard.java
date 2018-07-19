package com.micronesia.models.survey;

import android.support.annotation.NonNull;

import java.io.Serializable;

public interface Standard extends Serializable {

    long getId();

    long getGroupStandardId();

    @NonNull
    String getName();

}
