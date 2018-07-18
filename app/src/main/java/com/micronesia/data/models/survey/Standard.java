package com.micronesia.data.models.survey;

import android.support.annotation.NonNull;

public interface Standard {

    long getId();

    long getGroupStandardId();

    @NonNull
    String getName();

}
