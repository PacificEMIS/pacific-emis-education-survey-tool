package fm.doe.national.core.domain;

import java.util.List;

import fm.doe.national.core.data.model.Survey;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface SurveyInteractor {
    Single<List<Survey>> getAllSurveys();

    void setCurrentSurvey(Survey survey);

    void setCurrentSurvey(Survey survey, boolean shouldFetchProgress);

    Survey getCurrentSurvey();

    Observable<Survey> getSurveyProgressObservable();
}