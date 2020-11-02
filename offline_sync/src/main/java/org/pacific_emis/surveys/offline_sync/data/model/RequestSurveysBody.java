package org.pacific_emis.surveys.offline_sync.data.model;

import java.io.Serializable;

import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.SurveyType;

public class RequestSurveysBody implements Serializable {

    private String schoolId;
    private AppRegion appRegion;
    private SurveyType surveyType;
    private String surveyTag;

    public RequestSurveysBody() {
        // required for serialization
    }

    public RequestSurveysBody(String schoolId, AppRegion appRegion, SurveyType surveyType, String surveyTag) {
        this.schoolId = schoolId;
        this.appRegion = appRegion;
        this.surveyType = surveyType;
        this.surveyTag = surveyTag;
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

    public String getSurveyTag() {
        return surveyTag;
    }
}
