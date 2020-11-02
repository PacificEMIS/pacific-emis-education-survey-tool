package org.pacific_emis.surveys.offline_sync.data.model;

import androidx.annotation.NonNull;

import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableWashSurvey;

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
