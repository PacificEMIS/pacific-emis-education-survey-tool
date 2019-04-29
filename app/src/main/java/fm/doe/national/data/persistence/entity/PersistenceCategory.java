package fm.doe.national.data.persistence.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import fm.doe.national.data.persistence.new_model.Category;
import fm.doe.national.data.persistence.new_model.Standard;

@Entity(foreignKeys = @ForeignKey(entity = PersistenceSurvey.class, parentColumns = "uid", childColumns = "survey_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("survey_id")})
public class PersistenceCategory implements Category {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;

    @ColumnInfo(name = "survey_id")
    public long surveyId;

    @Ignore
    private List<Standard> standards;

    public PersistenceCategory(String title, long surveyId) {
        this.title = title;
        this.surveyId = surveyId;
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public List<Standard> getStandards() {
        return standards;
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
