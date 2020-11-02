package org.pacific_emis.surveys.offline_sync.data.model;

import java.io.Serializable;

import org.pacific_emis.surveys.core.data.model.Survey;

public interface ResponseSurveyBody extends Serializable {

    Survey getSurvey();

}
