package fm.doe.national.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.data.model.Survey;
import fm.doe.national.data.model.SurveyType;
import fm.doe.national.data.persistence.entity.relative.RelativeRoomSurvey;

public class MutableSurvey extends BaseMutableEntity implements Survey {

    private int version;
    private SurveyType surveyType;
    private Date date;
    private String schoolName;
    private String schoolId;
    private List<MutableCategory> categories;
    private MutableProgress progress = MutableProgress.createEmptyProgress();

    public static MutableSurvey toMutable(@NonNull Survey survey) {
        if (survey instanceof MutableSurvey) {
            return (MutableSurvey) survey;
        }
        return new MutableSurvey(survey);
    }

    public MutableSurvey() {
    }

    public MutableSurvey(@NonNull Survey other) {
        this.id = other.getId();
        this.version = other.getVersion();
        this.surveyType = other.getSurveyType();
        this.date = other.getDate();
        this.schoolName = other.getSchoolName();
        this.schoolId = other.getSchoolId();
        if (other.getCategories() != null) {
            this.categories = other.getCategories().stream().map(MutableCategory::new).collect(Collectors.toList());
        }
    }

    public MutableSurvey(@NonNull RelativeRoomSurvey relativeRoomSurvey) {
        this(relativeRoomSurvey.survey);
        categories = relativeRoomSurvey.categories.stream().map(MutableCategory::new).collect(Collectors.toList());
    }

    @Override
    public int getVersion() {
        return version;
    }

    @NonNull
    @Override
    public SurveyType getSurveyType() {
        return surveyType;
    }

    @Nullable
    @Override
    public Date getDate() {
        return date;
    }

    @Nullable
    @Override
    public String getSchoolName() {
        return schoolName;
    }

    @Nullable
    @Override
    public String getSchoolId() {
        return schoolId;
    }

    @NonNull
    @Override
    public List<MutableCategory> getCategories() {
        return categories;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setSurveyType(SurveyType surveyType) {
        this.surveyType = surveyType;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public void setCategories(List<MutableCategory> categories) {
        this.categories = categories;
    }

    @NonNull
    public MutableProgress getProgress() {
        return progress;
    }

    public void setProgress(MutableProgress progress) {
        this.progress = progress;
    }
}
