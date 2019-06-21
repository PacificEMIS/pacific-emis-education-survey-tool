package fm.doe.national.offline_sync.data.model;

import java.io.Serializable;

import fm.doe.national.core.data.model.Survey;

public class ResponseSurveyBody implements Serializable {

    private Survey survey;

    public ResponseSurveyBody(Survey survey) {
        this.survey = survey;
    }

    public Survey getSurvey() {
        return survey;
    }

}
