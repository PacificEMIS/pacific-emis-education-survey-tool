package org.pacific_emis.surveys.accreditation_core.data.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Date;

public interface ObservationInfo extends Serializable {
    @Nullable
    String getTeacherName();

    @Nullable
    String getGrade();

    @Nullable
    Integer getTotalStudentsPresent();

    @Nullable
    String getSubject();

    @Nullable
    Date getDate();
}
