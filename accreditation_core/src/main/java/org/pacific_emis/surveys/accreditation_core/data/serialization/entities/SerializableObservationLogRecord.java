package org.pacific_emis.surveys.accreditation_core.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.pacific_emis.surveys.accreditation_core.data.model.ObservationLogRecord;
import org.pacific_emis.surveys.accreditation_core.data.serialization.UtcDateConverter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.Date;

@Root(name = "record")
class SerializableObservationLogRecord implements ObservationLogRecord {

    @Nullable
    @Element(name = "teacherAction", required = false)
    String teacherAction;

    @Nullable
    @Element(name = "studentsAction", required = false)
    String studentsAction;

    @Nullable
    @Element(name = "date", required = false)
    @Convert(UtcDateConverter.class)
    Date date;

    @NonNull
    public static SerializableObservationLogRecord from(@NonNull ObservationLogRecord other) {
        if (other instanceof SerializableObservationLogRecord) {
            return (SerializableObservationLogRecord) other;
        }
        return new SerializableObservationLogRecord(other);
    }

    public SerializableObservationLogRecord() {
        // nothing
    }

    private SerializableObservationLogRecord(@NonNull ObservationLogRecord other) {
        this.teacherAction = other.getTeacherActions();
        this.studentsAction = other.getStudentsActions();
        this.date = other.getDate();
    }

    @Override
    public long getId() {
        return 0;
    }

    @Nullable
    @Override
    public String getTeacherActions() {
        return teacherAction;
    }

    @Nullable
    @Override
    public String getStudentsActions() {
        return studentsAction;
    }

    @Nullable
    @Override
    public Date getDate() {
        return date;
    }
}
