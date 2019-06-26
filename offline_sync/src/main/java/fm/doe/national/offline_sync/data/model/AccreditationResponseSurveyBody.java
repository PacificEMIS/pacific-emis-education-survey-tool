package fm.doe.national.offline_sync.data.model;

import androidx.annotation.NonNull;

import fm.doe.national.accreditation_core.data.model.mutable.MutableAccreditationSurvey;
import fm.doe.national.core.data.model.Survey;

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
