package org.pacific_emis.surveys.offline_sync.data.model;

import androidx.annotation.NonNull;

import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableAccreditationSurvey;
import org.pacific_emis.surveys.core.data.model.Survey;

public final class AccreditationResponseSurveyBody implements ResponseSurveyBody {

    @NonNull
    private MutableAccreditationSurvey accreditationSurvey;

    public AccreditationResponseSurveyBody() {
        // required for serialization
    }

    public AccreditationResponseSurveyBody(MutableAccreditationSurvey accreditationSurvey) {
        this.accreditationSurvey = accreditationSurvey;
    }

    @Override
    public Survey getSurvey() {
        return accreditationSurvey;
    }
}
