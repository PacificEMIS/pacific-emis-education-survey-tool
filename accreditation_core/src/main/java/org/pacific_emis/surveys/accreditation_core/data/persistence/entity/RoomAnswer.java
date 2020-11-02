package org.pacific_emis.surveys.accreditation_core.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import org.pacific_emis.surveys.accreditation_core.data.model.Answer;
import org.pacific_emis.surveys.accreditation_core.data.model.AnswerState;
import org.pacific_emis.surveys.core.data.model.Photo;

@Entity(foreignKeys = @ForeignKey(entity = RoomSubCriteria.class, parentColumns = "uid", childColumns = "sub_criteria_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("sub_criteria_id")})
public class RoomAnswer implements Answer {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "sub_criteria_id")
    public long subCriteriaId;

    public AnswerState state;

    @Nullable
    public String comment;

    public RoomAnswer(long subCriteriaId) {
        this.subCriteriaId = subCriteriaId;
        this.state = AnswerState.NOT_ANSWERED;
    }

    public RoomAnswer(@NonNull Answer otherAnswer) {
        this.uid = otherAnswer.getId();
        this.state = otherAnswer.getState();
        this.comment = otherAnswer.getComment();
    }

    @Override
    public AnswerState getState() {
        return state;
    }

    @Nullable
    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public long getId() {
        return uid;
    }

    @Nullable
    @Override
    public List<? extends Photo> getPhotos() {
        return null;
    }
}
