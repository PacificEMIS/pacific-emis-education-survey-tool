package org.pacific_emis.surveys.offline_sync.data.model;

import java.io.Serializable;
import java.util.List;

import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableAccreditationSurvey;
import org.pacific_emis.surveys.core.data.model.Survey;

public class ResponseAccreditationSurveysBody implements ResponseSurveysBody, Serializable {

    private List<MutableAccreditationSurvey> surveys;

    public ResponseAccreditationSurveysBody() {
        // required for serialization
    }

    public ResponseAccreditationSurveysBody(List<MutableAccreditationSurvey> surveys) {
        this.surveys = surveys;
    }

    @Override
    public List<? extends Survey> getSurveys() {
        return surveys;
    }
}
