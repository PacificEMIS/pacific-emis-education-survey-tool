package fm.doe.national.domain;

import java.util.List;

import fm.doe.national.data.model.Answer;
import fm.doe.national.data.model.mutable.MutableCategory;
import fm.doe.national.data.model.mutable.MutableCriteria;
import fm.doe.national.data.model.mutable.MutableStandard;
import fm.doe.national.data.model.mutable.MutableSurvey;
import fm.doe.national.data.model.recommendations.Recommendation;
import fm.doe.national.ui.screens.report.summary.SummaryViewData;
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

    Single<List<SummaryViewData>> requestSummary();

    Single<List<Recommendation>> requestRecommendations();

    Completable updateAnswer(Answer answer, long categoryId, long standardId, long criteriaId, long subCriteriaId);

    PublishSubject<MutableSurvey> getSurveyProgressSubject();

    PublishSubject<MutableCategory> getCategoryProgressSubject();

    PublishSubject<MutableStandard> getStandardProgressSubject();

    PublishSubject<MutableCriteria> getCriteriaProgressSubject();

}
