package fm.doe.national.data.persistence.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = PersistenceSurvey.class, parentColumns = "uid", childColumns = "survey_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("survey_id")})
public class PersistenceCategory {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;

    @ColumnInfo(name = "survey_id")
    public long surveyId;

    public PersistenceCategory(String title, long surveyId) {
        this.title = title;
        this.surveyId = surveyId;
    }
}
