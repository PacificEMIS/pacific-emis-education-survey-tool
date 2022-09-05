package org.pacific_emis.surveys.core.domain;

import java.util.List;

import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface SurveyInteractor {
    Single<List<Survey>> getAllSurveys(AppRegion appRegion);

    void setCurrentSurvey(Survey survey);

    void setCurrentSurvey(Survey survey, boolean shouldFetchProgress);

    Survey getCurrentSurvey();

    Single<List<Teacher>> loadTeachers(AppRegion appRegion);

    Observable<Survey> getSurveyProgressObservable();

    Completable completeSurveyIfNeed();
}