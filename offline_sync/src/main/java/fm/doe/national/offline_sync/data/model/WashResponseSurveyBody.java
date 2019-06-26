package fm.doe.national.offline_sync.data.model;

import androidx.annotation.NonNull;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.wash_core.data.model.mutable.MutableWashSurvey;

public final class WashResponseSurveyBody implements ResponseSurveyBody {

    @NonNull
    private MutableWashSurvey washSurvey;

    public WashResponseSurveyBody() {
        // required for serialization
    }

    public WashResponseSurveyBody(MutableWashSurvey washSurvey) {
        this.washSurvey = washSurvey;
    }

    @Override
    public Survey getSurvey() {
        return washSurvey;
    }
}
