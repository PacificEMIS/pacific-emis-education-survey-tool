package fm.doe.national.accreditation_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

import fm.doe.national.accreditation_core.data.model.ObservationInfo;
import fm.doe.national.core.data.model.ConflictResolveStrategy;

public class MutableObservationInfo implements ObservationInfo {

    @Nullable
    private String teacherName;
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
                                  @Nullable String grade,
                                  @Nullable Integer totalStudentsPresent,
                                  @Nullable String subject,
                                  @Nullable Date date) {
        this.teacherName = teacherName;
        this.grade = grade;
        this.totalStudentsPresent = totalStudentsPresent;
        this.subject = subject;
        this.date = date;
    }

    private MutableObservationInfo(@NonNull ObservationInfo other) {
        this.teacherName = other.getTeacherName();
        this.grade = other.getGrade();
        this.totalStudentsPresent = other.getTotalStudentsPresent();
        this.subject = other.getSubject();
        this.date = other.getDate();
    }

    public void setTeacherName(@Nullable String teacherName) {
        this.teacherName = teacherName;
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

    public void merge(@NonNull ObservationInfo other, @NonNull ConflictResolveStrategy strategy) {
        switch (strategy) {
            case MINE:
                // do nothing
                break;
            case THEIRS:
                if (other.getTeacherName() != null) {
                    this.teacherName = other.getTeacherName();
                }
                if (other.getGrade() != null) {
                    this.grade = other.getGrade();
                }
                if (other.getTotalStudentsPresent() != null) {
                    this.totalStudentsPresent = other.getTotalStudentsPresent();
                }
                if (other.getSubject() != null) {
                    this.subject = other.getSubject();
                }
                if (other.getDate() != null) {
                    this.date = other.getDate();
                }
                break;
        }
    }
}
