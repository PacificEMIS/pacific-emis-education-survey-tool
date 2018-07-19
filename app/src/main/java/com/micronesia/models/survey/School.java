package com.micronesia.models.survey;

import android.support.annotation.NonNull;

import java.io.Serializable;

public interface School extends Serializable {

    String getId();

    @NonNull
    String getName();

}
