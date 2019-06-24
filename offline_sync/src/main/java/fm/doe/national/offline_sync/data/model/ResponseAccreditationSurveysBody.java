package fm.doe.national.offline_sync.data.model;

import java.io.Serializable;
import java.util.List;

import fm.doe.national.accreditation_core.data.serialization.entities.SerializableAccreditationSurvey;
import fm.doe.national.core.data.model.Survey;

public class ResponseAccreditationSurveysBody implements ResponseSurveysBody, Serializable {

    private List<SerializableAccreditationSurvey> surveys;

    public ResponseAccreditationSurveysBody() {
        // required for serialization
    }

    public ResponseAccreditationSurveysBody(List<SerializableAccreditationSurvey> surveys) {
        this.surveys = surveys;
    }

    @Override
    public List<? extends Survey> getSurveys() {
        return surveys;
    }
}
