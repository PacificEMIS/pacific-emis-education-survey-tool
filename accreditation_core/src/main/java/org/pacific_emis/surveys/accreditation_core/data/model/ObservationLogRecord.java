package org.pacific_emis.surveys.accreditation_core.data.model;

import androidx.annotation.Nullable;

import java.util.Date;

import org.pacific_emis.surveys.core.data.model.IdentifiedObject;

public interface ObservationLogRecord extends IdentifiedObject {

    Date getDate();

    @Nullable
    String getTeacherActions();

    @Nullable
    String getStudentsActions();

}
