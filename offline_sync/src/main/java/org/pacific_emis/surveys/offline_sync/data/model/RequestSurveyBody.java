package org.pacific_emis.surveys.offline_sync.data.model;

import java.io.Serializable;

import org.pacific_emis.surveys.core.preferences.entities.SurveyType;

public class RequestSurveyBody implements Serializable {
    private long surveyId;
    private SurveyType surveyType;

    public RequestSurveyBody() {
        // required for serialization
    }

    public RequestSurveyBody(long surveyId, SurveyType surveyType) {
        this.surveyId = surveyId;
        this.surveyType = surveyType;
    }

    public long getSurveyId() {
        return surveyId;
    }

    public SurveyType getSurveyType() {
        return surveyType;
    }
}
