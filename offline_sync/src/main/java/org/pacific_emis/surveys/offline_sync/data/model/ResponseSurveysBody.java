package org.pacific_emis.surveys.offline_sync.data.model;

import java.util.List;

import org.pacific_emis.surveys.core.data.model.Survey;

public interface ResponseSurveysBody {
    List<? extends Survey> getSurveys();
}
