package fm.doe.national.accreditation_core.data.persistence.entity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import fm.doe.national.accreditation_core.data.model.Category;
import fm.doe.national.accreditation_core.data.model.EvaluationForm;
import fm.doe.national.core.data.model.Progress;
import fm.doe.national.accreditation_core.data.model.Standard;

@Entity(foreignKeys = @ForeignKey(entity = RoomAccreditationSurvey.class, parentColumns = "uid", childColumns = "survey_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("survey_id")})
public class RoomCategory implements Category {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;

    @ColumnInfo(name = "survey_id")
    public long surveyId;

    @ColumnInfo(name = "evaluation_form")
    public EvaluationForm evaluationForm;

    public RoomCategory(String title, long surveyId, EvaluationForm evaluationForm) {
        this.title = title;
        this.surveyId = surveyId;
        this.evaluationForm = evaluationForm;
    }

    public RoomCategory(@NonNull Category other) {
        this.uid = other.getId();
        this.title = other.getTitle();
        this.evaluationForm = other.getEvaluationForm();
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public List<? extends Standard> getStandards() {
        return null;
    }

    @Override
    public long getId() {
        return uid;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        throw new IllegalStateException();
    }

    @Override
    public EvaluationForm getEvaluationForm() {
        return evaluationForm;
    }
}
