package org.pacific_emis.surveys.core.domain;

import java.util.List;

import org.pacific_emis.surveys.core.data.model.Survey;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface SurveyInteractor {
    Single<List<Survey>> getAllSurveys();

    void setCurrentSurvey(Survey survey);

    void setCurrentSurvey(Survey survey, boolean shouldFetchProgress);

    Survey getCurrentSurvey();

    Observable<Survey> getSurveyProgressObservable();

    Completable completeSurveyIfNeed();
}