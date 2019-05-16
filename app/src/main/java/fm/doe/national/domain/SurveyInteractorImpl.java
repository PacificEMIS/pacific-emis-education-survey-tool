package fm.doe.national.domain;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.model.Answer;
import fm.doe.national.data.model.AnswerState;
import fm.doe.national.data.model.Category;
import fm.doe.national.data.model.Criteria;
import fm.doe.national.data.model.Standard;
import fm.doe.national.data.model.SubCriteria;
import fm.doe.national.data.model.mutable.MutableAnswer;
import fm.doe.national.data.model.mutable.MutableCategory;
import fm.doe.national.data.model.mutable.MutableCriteria;
import fm.doe.national.data.model.mutable.MutableStandard;
import fm.doe.national.data.model.mutable.MutableSubCriteria;
import fm.doe.national.data.model.mutable.MutableSurvey;
import fm.doe.national.ui.screens.report.recommendations.CategoryRecommendation;
import fm.doe.national.ui.screens.report.recommendations.CriteriaRecommendation;
import fm.doe.national.ui.screens.report.recommendations.Recommendation;
import fm.doe.national.ui.screens.report.recommendations.StandardRecommendation;
import fm.doe.national.ui.screens.report.recommendations.SubCriteriaRecommendation;
import fm.doe.national.ui.screens.report.summary.SummaryViewData;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public class SurveyInteractorImpl implements SurveyInteractor {

    private final DataSource dataSource;
    private final PublishSubject<MutableSurvey> surveyPublishSubject = PublishSubject.create();
    private final PublishSubject<MutableCategory> categoryPublishSubject = PublishSubject.create();
    private final PublishSubject<MutableStandard> standardPublishSubject = PublishSubject.create();
    private final PublishSubject<MutableCriteria> criteriaPublishSubject = PublishSubject.create();

    private MutableSurvey survey;

    public SurveyInteractorImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Single<List<MutableSurvey>> getAllSurveys() {
        return dataSource.loadAllSurveys()
                .flatMapObservable(Observable::fromIterable)
                .map(survey -> {
                    MutableSurvey mutableSurvey = new MutableSurvey(survey);
                    initProgress(mutableSurvey);
                    return mutableSurvey;
                })
                .toList();
    }

    @Override
    public void setCurrentSurvey(MutableSurvey survey) {
        setCurrentSurvey(survey, false);
    }

    @Override
    public void setCurrentSurvey(MutableSurvey survey, boolean shouldFetchProgress) {
        this.survey = survey;
        if (shouldFetchProgress) {
            initProgress(this.survey);
        }
    }

    @Override
    public MutableSurvey getCurrentSurvey() {
        return survey;
    }

    private void initProgress(MutableSurvey survey) {
        for (MutableCategory category : survey.getCategories()) {
            initProgress(category);
            survey.getProgress().add(category.getProgress());
        }
    }

    private void initProgress(MutableCategory category) {
        for (MutableStandard standard : category.getStandards()) {
            initProgress(standard);
            category.getProgress().add(standard.getProgress());
        }
    }

    private void initProgress(MutableStandard standard) {
        for (MutableCriteria criteria : standard.getCriterias()) {
            initProgress(criteria);
            standard.getProgress().add(criteria.getProgress());
        }
    }

    private void initProgress(MutableCriteria criteria) {
        criteria.getProgress().total = criteria.getSubCriterias().size();
        for (MutableSubCriteria subCriteria : criteria.getSubCriterias()) {
            if (subCriteria.getAnswer().getState() != AnswerState.NOT_ANSWERED) {
                criteria.getProgress().completed++;
            }
        }
    }

    @Override
    public Single<List<MutableCategory>> requestCategories() {
        return Single.fromCallable(() -> survey.getCategories());
    }

    @Override
    public Single<List<MutableStandard>> requestStandards(long categoryId) {
        return requestCategories()
                .flatMapObservable(Observable::fromIterable)
                .filter(cat -> cat.getId() == categoryId)
                .firstOrError()
                .map(MutableCategory::getStandards);
    }

    @Override
    public Single<List<MutableCriteria>> requestCriterias(long categoryId, long standardId) {
        return requestStandards(categoryId)
                .flatMapObservable(Observable::fromIterable)
                .filter(it -> it.getId() == standardId)
                .firstOrError()
                .map(MutableStandard::getCriterias);
    }

    @Override
    public Single<List<SummaryViewData>> requestSummary() {
        return requestCategories()
                .flatMapObservable(Observable::fromIterable)
                .flatMap(category -> Observable.fromIterable(category.getStandards())
                        .map(standard -> {
                            List<SummaryViewData.CriteriaSummaryViewData> criteriaSummaryViewDataList = new ArrayList<>();
                            int totalByStandard = 0;
                            for (Criteria criteria : standard.getCriterias()) {
                                int totalByCriteria = 0;
                                boolean[] positivesArray = new boolean[criteria.getSubCriterias().size()];
                                for (int i = 0; i < criteria.getSubCriterias().size(); i++) {
                                    switch (criteria.getSubCriterias().get(i).getAnswer().getState()) {
                                        case POSITIVE:
                                            totalByCriteria++;
                                            totalByStandard++;
                                            positivesArray[i] = true;
                                            break;
                                        default:
                                            positivesArray[i] = false;
                                            break;
                                    }
                                }
                                criteriaSummaryViewDataList.add(
                                        new SummaryViewData.CriteriaSummaryViewData(
                                                criteria.getSuffix(),
                                                positivesArray,
                                                totalByCriteria
                                        )
                                );
                            }
                            return new SummaryViewData(category, standard, totalByStandard, criteriaSummaryViewDataList);
                        })
                ).toList();
    }

    @SuppressLint("CheckResult")
    @Override
    public Completable updateAnswer(Answer answer, long categoryId, long standardId, long criteriaId, long subCriteriaId) {
        return dataSource.updateAnswer(answer, subCriteriaId)
                .flatMapCompletable(updatedAnswer -> Completable.fromAction(() -> notifyProgressChanged(
                        new MutableAnswer(updatedAnswer),
                        categoryId,
                        standardId,
                        criteriaId,
                        subCriteriaId
                )));
    }

    private void notifyProgressChanged(MutableAnswer answer,
                                       long categoryId,
                                       long standardId,
                                       long criteriaId,
                                       long subCriteriaId) {
        int delta = 0;
        for (MutableCategory category : survey.getCategories()) {
            if (category.getId() == categoryId) {
                delta = findProgressDeltaAndNotify(answer, category, standardId, criteriaId, subCriteriaId);
                break;
            }
        }

        survey.getProgress().completed += delta;
        surveyPublishSubject.onNext(survey);
    }

    private int findProgressDeltaAndNotify(MutableAnswer answer,
                                           MutableCategory category,
                                           long standardId,
                                           long criteriaId,
                                           long subCriteriaId) {
        int delta = 0;
        for (MutableStandard standard : category.getStandards()) {
            if (standard.getId() == standardId) {
                delta = findProgressDeltaAndNotify(answer, standard, criteriaId, subCriteriaId);
                break;
            }
        }
        category.getProgress().completed += delta;
        categoryPublishSubject.onNext(category);
        return delta;
    }

    private int findProgressDeltaAndNotify(MutableAnswer answer,
                                           MutableStandard standard,
                                           long criteriaId,
                                           long subCriteriaId) {
        int delta = 0;
        for (MutableCriteria criteria : standard.getCriterias()) {
            if (criteria.getId() == criteriaId) {
                delta = findProgressDeltaAndNotify(answer, criteria, subCriteriaId);
                break;
            }
        }
        standard.getProgress().completed += delta;
        standardPublishSubject.onNext(standard);
        return delta;
    }

    private int findProgressDeltaAndNotify(MutableAnswer answer,
                                           MutableCriteria criteria,
                                           long subCriteriaId) {
        int oldCompleted = criteria.getProgress().completed;
        int completed = 0;
        for (MutableSubCriteria subCriteria : criteria.getSubCriterias()) {
            if (subCriteria.getId() == subCriteriaId) {
                subCriteria.setAnswer(answer);
            }
            if (subCriteria.getAnswer().getState() != AnswerState.NOT_ANSWERED) {
                completed++;
            }
        }
        criteria.getProgress().completed = completed;
        criteriaPublishSubject.onNext(criteria);
        return completed - oldCompleted;
    }

    @Override
    public PublishSubject<MutableSurvey> getSurveyProgressSubject() {
        return surveyPublishSubject;
    }

    @Override
    public PublishSubject<MutableCategory> getCategoryProgressSubject() {
        return categoryPublishSubject;
    }

    @Override
    public PublishSubject<MutableStandard> getStandardProgressSubject() {
        return standardPublishSubject;
    }

    @Override
    public PublishSubject<MutableCriteria> getCriteriaProgressSubject() {
        return criteriaPublishSubject;
    }

    @Override
    public Single<List<Recommendation>> requestRecommendations() {
        return requestCategories()
                .map(this::generateCategoryRecommendations);
    }

    private List<Recommendation> generateCategoryRecommendations(List<? extends Category> items) {
        List<Recommendation> recommendations = new ArrayList<>();
        for (Category item : items) {
            List<Recommendation> standardRecommendations = generateStandardRecommendations(item.getStandards());
            if (!standardRecommendations.isEmpty()) {
                recommendations.add(new CategoryRecommendation(item));
                recommendations.addAll(standardRecommendations);
            }
        }
        return recommendations;
    }

    private List<Recommendation> generateStandardRecommendations(List<? extends Standard> items) {
        List<Recommendation> recommendations = new ArrayList<>();
        for (Standard item : items) {
            List<Recommendation> criteriaRecommendations = generateCriteriaRecommendations(item.getCriterias());
            if (!criteriaRecommendations.isEmpty()) {
                recommendations.add(new StandardRecommendation(item));
                recommendations.addAll(criteriaRecommendations);
            }
        }
        return recommendations;
    }

    private List<Recommendation> generateCriteriaRecommendations(List<? extends Criteria> items) {
        List<Recommendation> recommendations = new ArrayList<>();
        for (Criteria item : items) {
            List<Recommendation> subCriteriaRecommendations = generateSubCriteriaRecommendations(item.getSubCriterias());
            if (!subCriteriaRecommendations.isEmpty()) {
                recommendations.add(new CriteriaRecommendation(item));
                recommendations.addAll(subCriteriaRecommendations);
            }
        }
        return recommendations;
    }

    private List<Recommendation> generateSubCriteriaRecommendations(List<? extends SubCriteria> items) {
        List<Recommendation> recommendations = new ArrayList<>();
        for (SubCriteria item : items) {
            if (item.getAnswer().getState() != AnswerState.POSITIVE) {
                recommendations.add(new SubCriteriaRecommendation(item));
            }
        }
        return recommendations;
    }
}
