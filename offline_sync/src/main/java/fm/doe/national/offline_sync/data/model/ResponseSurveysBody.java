package fm.doe.national.offline_sync.data.model;

import java.io.Serializable;
import java.util.List;

import fm.doe.national.core.data.model.Survey;

public class ResponseSurveysBody implements Serializable {

    private List<Survey> surveys;

    public ResponseSurveysBody(List<Survey> surveys) {
        this.surveys = surveys;
    }

    public List<Survey> getSurveys() {
        return surveys;
    }
}
