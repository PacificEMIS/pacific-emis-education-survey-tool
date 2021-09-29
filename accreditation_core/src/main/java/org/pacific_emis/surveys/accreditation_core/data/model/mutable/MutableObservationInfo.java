package org.pacific_emis.surveys.accreditation_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.pacific_emis.surveys.accreditation_core.data.model.ObservationInfo;

import java.util.Date;

public class MutableObservationInfo implements ObservationInfo {

    @Nullable
    private String teacherName;
    @Nullable
    private Integer teacherId;
    @Nullable
    private String grade;
    @Nullable
    private Integer totalStudentsPresent;
    @Nullable
    private String subject;
    @Nullable
    private Date date;

    public static MutableObservationInfo from(@NonNull ObservationInfo other) {
        if (other instanceof MutableObservationInfo) {
            return (MutableObservationInfo) other;
        }
        return new MutableObservationInfo(other);
    }

    public MutableObservationInfo() {
        // nothing
    }

    public MutableObservationInfo(@Nullable String teacherName,
                                  @Nullable Integer teacherId,
                                  @Nullable String grade,
                                  @Nullable Integer totalStudentsPresent,
                                  @Nullable String subject,
                                  @Nullable Date date) {
        this.teacherName = teacherName;
        this.teacherId = teacherId;
        this.grade = grade;
        this.totalStudentsPresent = totalStudentsPresent;
        this.subject = subject;
        this.date = date;
    }

    private MutableObservationInfo(@NonNull ObservationInfo other) {
        this.teacherName = other.getTeacherName();
        this.teacherId = other.getTeacherId();
        this.grade = other.getGrade();
        this.totalStudentsPresent = other.getTotalStudentsPresent();
        this.subject = other.getSubject();
        this.date = other.getDate();
    }

    public void setTeacherName(@Nullable String teacherName) {
        this.teacherName = teacherName;
    }

    public void setTeacherId(@Nullable Integer teacherId) {
        this.teacherId = teacherId;
    }

    public void setGrade(@Nullable String grade) {
        this.grade = grade;
    }

    public void setTotalStudentsPresent(@Nullable Integer totalStudentsPresent) {
        this.totalStudentsPresent = totalStudentsPresent;
    }

    public void setSubject(@Nullable String subject) {
        this.subject = subject;
    }

    public void setDate(@Nullable Date date) {
        this.date = date;
    }

    @Nullable
    @Override
    public String getTeacherName() {
        return teacherName;
    }

    @Nullable
    @Override
    public Integer getTeacherId() {
        return teacherId;
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

    public boolean merge(@NonNull ObservationInfo other) {
        boolean haveChanges = false;

        if (teacherName == null) {
            this.teacherName = other.getTeacherName();
            haveChanges = true;
        }

        if (teacherId == null) {
            this.teacherId = other.getTeacherId();
            haveChanges = true;
        }

        if (grade == null) {
            this.grade = other.getGrade();
            haveChanges = true;
        }

        if (totalStudentsPresent == null) {
            this.totalStudentsPresent = other.getTotalStudentsPresent();
            haveChanges = true;
        }

        if (subject == null) {
            this.subject = other.getSubject();
            haveChanges = true;
        }

        if (date == null) {
            this.date = other.getDate();
            haveChanges = true;
        }

        return haveChanges;
    }
}
