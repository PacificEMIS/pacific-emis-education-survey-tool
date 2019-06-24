package fm.doe.national.offline_sync.data.model;

import java.util.List;

import fm.doe.national.core.data.model.Survey;

public interface ResponseSurveysBody {
    List<? extends Survey> getSurveys();
}
