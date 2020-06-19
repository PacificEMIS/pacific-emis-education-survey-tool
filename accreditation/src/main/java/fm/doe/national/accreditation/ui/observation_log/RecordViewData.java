package fm.doe.national.accreditation.ui.observation_log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.Objects;

public class RecordViewData {
    @NonNull
    private Date date;
    @Nullable
    private String teacherActions;
    @Nullable
    private String studentsActions;

    public RecordViewData(@NonNull Date date, @Nullable String teacherActions, @Nullable String studentsActions) {
        this.date = date;
        this.teacherActions = teacherActions;
        this.studentsActions = studentsActions;
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
        RecordViewData that = (RecordViewData) o;
        return date.equals(that.getDate()) &&
                Objects.equals(teacherActions, that.getTeacherActions()) &&
                Objects.equals(studentsActions, that.getStudentsActions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getTeacherActions(), getStudentsActions());
    }
}
