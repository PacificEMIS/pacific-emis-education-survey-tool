package fm.doe.national.offline_sync.data.model;

import java.io.Serializable;

import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.SurveyType;

public class RequestSurveysBody implements Serializable {

    private String schoolId;
    private AppRegion appRegion;
    private SurveyType surveyType;

    public RequestSurveysBody() {
        // required for serialization
    }

    public RequestSurveysBody(String schoolId, AppRegion appRegion, SurveyType surveyType) {
        this.schoolId = schoolId;
        this.appRegion = appRegion;
        this.surveyType = surveyType;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public AppRegion getAppRegion() {
        return appRegion;
    }

    public SurveyType getSurveyType() {
        return surveyType;
    }
}
