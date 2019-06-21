package fm.doe.national.offline_sync.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.SurveyType;

public class TmpSurvey implements Survey {

    private int version;
    private SurveyType surveyType;
    private AppRegion appRegion;

    @Nullable
    private Date date;

    @Nullable
    private String schoolName;

    @Nullable
    private String schoolId;

    public TmpSurvey(int version, SurveyType surveyType, AppRegion appRegion, @Nullable Date date, @Nullable String schoolName, @Nullable String schoolId) {
        this.version = version;
        this.surveyType = surveyType;
        this.appRegion = appRegion;
        this.date = date;
        this.schoolName = schoolName;
        this.schoolId = schoolId;
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

    @Override
    public long getId() {
        return 0;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        return null;
    }
}
