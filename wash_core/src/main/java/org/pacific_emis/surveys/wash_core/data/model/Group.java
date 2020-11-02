package org.pacific_emis.surveys.wash_core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import org.pacific_emis.surveys.core.data.model.IdentifiedObject;
import org.pacific_emis.surveys.core.data.model.Progressable;

public interface Group extends Progressable, IdentifiedObject {

    @NonNull
    String getTitle();

    @NonNull
    String getPrefix();

    @Nullable
    List<? extends SubGroup> getSubGroups();

}
