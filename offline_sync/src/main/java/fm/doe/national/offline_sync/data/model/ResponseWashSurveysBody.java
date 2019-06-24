package fm.doe.national.offline_sync.data.model;

import java.io.Serializable;
import java.util.List;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.wash_core.data.serialization.model.SerializableWashSurvey;

public class ResponseWashSurveysBody implements ResponseSurveysBody, Serializable {

    private List<SerializableWashSurvey> surveys;

    public ResponseWashSurveysBody() {
        // required for serialization
    }

    public ResponseWashSurveysBody(List<SerializableWashSurvey> surveys) {
        this.surveys = surveys;
    }

    @Override
    public List<? extends Survey> getSurveys() {
        return surveys;
    }
}
