package fm.doe.national.domain;

import java.util.List;

import fm.doe.national.data.model.mutable.MutableAnswer;
import fm.doe.national.data.model.mutable.MutableCategory;
import fm.doe.national.data.model.mutable.MutableCriteria;
import fm.doe.national.data.model.mutable.MutableStandard;
import fm.doe.national.data.model.mutable.MutableSurvey;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public interface SurveyInteractor {

    Single<List<MutableSurvey>> getAllSurveys();

    void setCurrentSurvey(MutableSurvey survey);

    void setCurrentSurvey(MutableSurvey survey, boolean shouldFetchProgress);

    MutableSurvey getCurrentSurvey();

    Single<List<MutableCategory>> requestCategories();

    Single<List<MutableStandard>> requestStandards(long categoryId);

    Single<List<MutableCriteria>> requestCriterias(long categoryId, long standardId);

    Single<Object> requestSummaryObject();

    Completable updateAnswer(MutableAnswer answer, long categoryId, long standardId, long criteriaId, long subCriteriaId);

    PublishSubject<MutableSurvey> getSurveyProgressSubject();

    PublishSubject<MutableCategory> getCategoryProgressSubject();

    PublishSubject<MutableStandard> getStandardProgressSubject();

    PublishSubject<MutableCriteria> getCriteriaProgressSubject();

}
