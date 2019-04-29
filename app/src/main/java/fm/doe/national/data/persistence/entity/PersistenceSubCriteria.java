package fm.doe.national.data.persistence.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import fm.doe.national.data.persistence.new_model.Answer;
import fm.doe.national.data.persistence.new_model.SubCriteria;

@Entity(foreignKeys = @ForeignKey(entity = PersistenceCriteria.class, parentColumns = "uid", childColumns = "criteria_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("criteria_id")})
public class PersistenceSubCriteria implements SubCriteria {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;
    public String suffix;

    @Nullable
    @ColumnInfo(name = "interview_question")
    public String interviewQuestion;

    @Nullable
    public String hint;

    @Ignore
    @Nullable
    private Answer answer;

    @ColumnInfo(name = "criteria_id")
    public long criteriaId;

    public PersistenceSubCriteria(String title, long criteriaId, String suffix) {
        this.title = title;
        this.criteriaId = criteriaId;
        this.suffix = suffix;
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
        return answer;
    }

    @Override
    public long getId() {
        return uid;
    }

    @Override
    public void setId(long newId) {
        uid = newId;
    }
}