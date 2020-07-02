package fm.doe.national.offline_sync.data.model;

import java.io.Serializable;

import fm.doe.national.core.data.model.Survey;

public interface ResponseSurveyBody extends Serializable {

    Survey getSurvey();

}
