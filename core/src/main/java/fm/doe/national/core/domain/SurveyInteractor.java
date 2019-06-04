package fm.doe.national.core.domain;

import java.util.List;

import fm.doe.national.core.data.model.Survey;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public interface SurveyInteractor {
    Single<List<Survey>> getAllSurveys();

    void setCurrentSurvey(Survey survey);

    void setCurrentSurvey(Survey survey, boolean shouldFetchProgress);

    Survey getCurrentSurvey();

    PublishSubject<Survey> getSurveyProgressSubject();
}