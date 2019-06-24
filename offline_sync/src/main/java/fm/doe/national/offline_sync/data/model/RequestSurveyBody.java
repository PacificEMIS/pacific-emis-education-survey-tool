package fm.doe.national.offline_sync.data.model;

import java.io.Serializable;

public class RequestSurveyBody implements Serializable {
    private long surveyId;

    public RequestSurveyBody() {
        // required for serialization
    }

    public RequestSurveyBody(long surveyId) {
        this.surveyId = surveyId;
    }

    public long getSurveyId() {
        return surveyId;
    }
}
