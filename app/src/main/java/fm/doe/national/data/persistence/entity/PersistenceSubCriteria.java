package fm.doe.national.data.persistence.entity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = PersistenceCriteria.class, parentColumns = "uid", childColumns = "criteria_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("criteria_id")})
public class PersistenceSubCriteria {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;
    public String suffix;

    @Nullable
    @ColumnInfo(name = "interview_question")
    public String interviewQuestion;

    @Nullable
    public String hint;



    @Nullable
    @ColumnInfo(name = "answer_as_string")
    public String answerAsString;

    @ColumnInfo(name = "criteria_id")
    public long criteriaId;

    public PersistenceSubCriteria(String title, long criteriaId, String suffix) {
        this.title = title;
        this.criteriaId = criteriaId;
        this.suffix = suffix;
    }

}