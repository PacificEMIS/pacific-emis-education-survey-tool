package fm.doe.national.wash_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.model.mutable.BaseMutableEntity;
import fm.doe.national.core.data.model.mutable.MutableProgress;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.SurveyType;
import fm.doe.national.wash_core.data.model.WashSurvey;

public class MutableWashSurvey extends BaseMutableEntity implements WashSurvey {

    @Nullable
    private List<MutableGroup> groups;

    private int version;

    @NonNull
    private SurveyType surveyType;

    @Nullable
    private Date date;

    @Nullable
    private String schoolName;

    @Nullable
    private String schoolId;

    @NonNull
    private AppRegion appRegion;

    @NonNull
    private MutableProgress progress;

    public MutableWashSurvey(WashSurvey other) {
        this(other.getVersion(), other.getSurveyType(), other.getAppRegion());

        this.id = other.getId();
        this.date = other.getDate();
        this.schoolId = other.getSchoolId();
        this.schoolName = other.getSchoolName();

        if (other.getGroups() != null) {
            this.groups = other.getGroups().stream().map(MutableGroup::new).collect(Collectors.toList());
        }
    }

    public MutableWashSurvey(int version, @NonNull SurveyType surveyType, @NonNull AppRegion appRegion) {
        this.version = version;
        this.surveyType = surveyType;
        this.appRegion = appRegion;
        this.progress = MutableProgress.createEmptyProgress();
    }

    @Nullable
    @Override
    public List<MutableGroup> getGroups() {
        return groups;
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
    public AppRegion getAppRegion() {
        return appRegion;
    }

    @NonNull
    @Override
    public MutableProgress getProgress() {
        return progress;
    }

    public void setGroups(@Nullable List<MutableGroup> groups) {
        this.groups = groups;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setSurveyType(@NonNull SurveyType surveyType) {
        this.surveyType = surveyType;
    }

    public void setDate(@Nullable Date date) {
        this.date = date;
    }

    public void setSchoolName(@Nullable String schoolName) {
        this.schoolName = schoolName;
    }

    public void setSchoolId(@Nullable String schoolId) {
        this.schoolId = schoolId;
    }

    public void setAppRegion(@NonNull AppRegion appRegion) {
        this.appRegion = appRegion;
    }

    public void setProgress(@NonNull MutableProgress progress) {
        this.progress = progress;
    }
}
