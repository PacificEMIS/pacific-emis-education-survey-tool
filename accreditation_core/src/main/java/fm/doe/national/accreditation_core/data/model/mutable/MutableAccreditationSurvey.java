package fm.doe.national.accreditation_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.core.data.model.mutable.BaseMutableEntity;
import fm.doe.national.core.data.model.mutable.MutableProgress;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.SurveyType;

public class MutableAccreditationSurvey extends BaseMutableEntity implements AccreditationSurvey {

    private int version;
    private SurveyType surveyType;
    private AppRegion appRegion;
    private Date date;
    private String schoolName;
    private String schoolId;
    private List<MutableCategory> categories;
    private MutableProgress progress = MutableProgress.createEmptyProgress();

    public static MutableAccreditationSurvey toMutable(@NonNull AccreditationSurvey accreditationSurvey) {
        if (accreditationSurvey instanceof MutableAccreditationSurvey) {
            return (MutableAccreditationSurvey) accreditationSurvey;
        }
        return new MutableAccreditationSurvey(accreditationSurvey);
    }

    public MutableAccreditationSurvey() {
    }

    public MutableAccreditationSurvey(@NonNull AccreditationSurvey other) {
        this.id = other.getId();
        this.version = other.getVersion();
        this.surveyType = other.getSurveyType();
        this.date = other.getDate();
        this.schoolName = other.getSchoolName();
        this.schoolId = other.getSchoolId();
        this.appRegion = other.getAppRegion();
        if (other.getCategories() != null) {
            this.categories = other.getCategories().stream().map(MutableCategory::new).collect(Collectors.toList());
        }
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

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        return appRegion;
    }

    public void setAppRegion(AppRegion appRegion) {
        this.appRegion = appRegion;
    }
}
