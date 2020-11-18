package org.pacific_emis.surveys.accreditation_core.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.pacific_emis.surveys.accreditation_core.data.model.ObservationInfo;
import org.pacific_emis.surveys.accreditation_core.data.serialization.UtcDateConverter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.Date;

@Root(name = "info")
class SerializableObservationInfo implements ObservationInfo {

    @Nullable
    @Element(name = "teacherName", required = false)
    String teacherName;

    @Nullable
    @Element(name = "grade", required = false)
    String grade;

    @Nullable
    @Element(name = "totalStudentsPresent", required = false)
    Integer totalStudentsPresent;

    @Nullable
    @Element(name = "subject", required = false)
    String subject;

    @Nullable
    @Element(name = "date", required = false)
    @Convert(UtcDateConverter.class)
    Date date;

    public SerializableObservationInfo() {
        // nothing
    }

    public SerializableObservationInfo(@NonNull ObservationInfo other) {
        this.teacherName = other.getTeacherName();
        this.grade = other.getGrade();
        this.totalStudentsPresent = other.getTotalStudentsPresent();
        this.subject = other.getSubject();
        this.date = other.getDate();
    }

    @Nullable
    @Override
    public String getTeacherName() {
        return teacherName;
    }

    @Nullable
    @Override
    public String getGrade() {
        return grade;
    }

    @Nullable
    @Override
    public Integer getTotalStudentsPresent() {
        return totalStudentsPresent;
    }

    @Nullable
    @Override
    public String getSubject() {
        return subject;
    }

    @Nullable
    @Override
    public Date getDate() {
        return date;
    }
}
