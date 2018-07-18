package com.micronesia.data.models.survey;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public interface GroupStandard extends Serializable {

    long getId();

    @NonNull
    String getName();

    List<Standard> getStandards();

}
