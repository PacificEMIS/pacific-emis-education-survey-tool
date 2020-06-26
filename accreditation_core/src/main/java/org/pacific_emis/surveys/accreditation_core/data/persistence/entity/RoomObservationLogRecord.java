package org.pacific_emis.surveys.accreditation_core.data.persistence.entity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import org.pacific_emis.surveys.accreditation_core.data.model.ObservationLogRecord;

@Entity(foreignKeys = @ForeignKey(entity = RoomCategory.class,
        parentColumns = "uid",
        childColumns = "category_id",
        onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("category_id")})
public class RoomObservationLogRecord implements ObservationLogRecord {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "category_id")
    public long categoryId;

    @Nullable
    @ColumnInfo(name = "date")
    public Date date;

    @Nullable
    @ColumnInfo(name = "teacher_action")
    public String teacherAction;

    @Nullable
    @ColumnInfo(name = "students_action")
    public String studentsAction;

    @NonNull
    public static RoomObservationLogRecord from(@NonNull ObservationLogRecord other) {
        if (other instanceof RoomObservationLogRecord) {
            return (RoomObservationLogRecord) other;
        }
        return new RoomObservationLogRecord(other);
    }

    public RoomObservationLogRecord(long categoryId,
                                    @Nullable Date date,
                                    @Nullable String teacherAction,
                                    @Nullable String studentsAction) {
        this.categoryId = categoryId;
        this.date = date;
        this.teacherAction = teacherAction;
        this.studentsAction = studentsAction;
    }

    private RoomObservationLogRecord(@NonNull ObservationLogRecord other) {
        this.uid = other.getId();
        this.date = other.getDate();
        this.teacherAction = other.getTeacherActions();
        this.studentsAction = other.getStudentsActions();
    }

    @Override
    public long getId() {
        return uid;
    }

    @Override
    public Date getDate() {
        return date;
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
}
