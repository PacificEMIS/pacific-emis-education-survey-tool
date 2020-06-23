package fm.doe.national.accreditation_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.Objects;

import fm.doe.national.accreditation_core.data.model.ObservationLogRecord;

public class MutableObservationLogRecord implements ObservationLogRecord {

    private long id;
    @NonNull
    private Date date;
    @Nullable
    private String teacherActions;
    @Nullable
    private String studentsActions;

    @NonNull
    public static MutableObservationLogRecord from(@NonNull ObservationLogRecord other) {
        if (other instanceof MutableObservationLogRecord) {
            return (MutableObservationLogRecord) other;
        }
        return new MutableObservationLogRecord(other);
    }

    private MutableObservationLogRecord(@NonNull ObservationLogRecord other) {
        this(other.getId(), other.getDate(), other.getTeacherActions(), other.getStudentsActions());
    }

    public MutableObservationLogRecord(long id, @NonNull Date date) {
        this(id, date, null, null);
    }

    public MutableObservationLogRecord(long id,
                                       @NonNull Date date,
                                       @Nullable String teacherActions,
                                       @Nullable String studentsActions) {
        this.id = id;
        this.date = date;
        this.teacherActions = teacherActions;
        this.studentsActions = studentsActions;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    @Nullable
    public String getTeacherActions() {
        return teacherActions;
    }

    public void setTeacherActions(@Nullable String teacherActions) {
        this.teacherActions = teacherActions;
    }

    @Nullable
    public String getStudentsActions() {
        return studentsActions;
    }

    public void setStudentsActions(@Nullable String studentsActions) {
        this.studentsActions = studentsActions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutableObservationLogRecord that = (MutableObservationLogRecord) o;
        return id == that.id &&
                date.equals(that.date) &&
                Objects.equals(teacherActions, that.teacherActions) &&
                Objects.equals(studentsActions, that.studentsActions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, teacherActions, studentsActions);
    }
}
