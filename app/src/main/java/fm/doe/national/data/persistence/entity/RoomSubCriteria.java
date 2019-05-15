package fm.doe.national.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import fm.doe.national.data.model.Answer;
import fm.doe.national.data.model.SubCriteria;

@Entity(foreignKeys = @ForeignKey(entity = RoomCriteria.class, parentColumns = "uid", childColumns = "criteria_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("criteria_id")})
public class RoomSubCriteria implements SubCriteria {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;
    public String suffix;

    @Nullable
    @ColumnInfo(name = "interview_question")
    public String interviewQuestion;

    @Nullable
    public String hint;

    @ColumnInfo(name = "criteria_id")
    public long criteriaId;

    public RoomSubCriteria(String title, long criteriaId, String suffix) {
        this.title = title;
        this.criteriaId = criteriaId;
        this.suffix = suffix;
    }

    public RoomSubCriteria(@NonNull SubCriteria otherSubCriteria) {
        this.uid = otherSubCriteria.getId();
        this.suffix = otherSubCriteria.getSuffix();
        this.title = otherSubCriteria.getTitle();
        this.interviewQuestion = otherSubCriteria.getInterviewQuestions();
        this.hint = otherSubCriteria.getHint();
    }

    @NonNull
    @Override
    public String getSuffix() {
        return suffix;
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getInterviewQuestions() {
        return interviewQuestion;
    }

    @Nullable
    @Override
    public String getHint() {
        return hint;
    }

    @Nullable
    @Override
    public Answer getAnswer() {
        return null;
    }

    @Override
    public long getId() {
        return uid;
    }

}