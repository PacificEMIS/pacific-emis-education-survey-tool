package com.micronesia.data.models.survey;

import android.support.annotation.NonNull;

public interface Criteria {

    long getId();

    long getStandardId();

    @NonNull
    String getName();

}
