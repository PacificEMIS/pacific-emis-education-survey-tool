package fm.doe.national.domain;

import android.annotation.SuppressLint;

import java.util.List;

import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.model.AnswerState;
import fm.doe.national.data.model.mutable.MutableAnswer;
import fm.doe.national.data.model.mutable.MutableCategory;
import fm.doe.national.data.model.mutable.MutableCriteria;
import fm.doe.national.data.model.mutable.MutableStandard;
import fm.doe.national.data.model.mutable.MutableSubCriteria;
import fm.doe.national.data.model.mutable.MutableSurvey;
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
                    initProgress(survey);
                    return survey;
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
    public Single<Object> requestSummaryObject() {
        return null;
    }

    @SuppressLint("CheckResult")
    @Override
    public Completable updateAnswer(MutableAnswer answer, long categoryId, long standardId, long criteriaId, long subCriteriaId) {
        return dataSource.updateAnswer(answer, subCriteriaId)
                .flatMapCompletable(updatedAnswer -> Completable.fromAction(() -> notifyProgressChanged(
                        updatedAnswer,
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

}
