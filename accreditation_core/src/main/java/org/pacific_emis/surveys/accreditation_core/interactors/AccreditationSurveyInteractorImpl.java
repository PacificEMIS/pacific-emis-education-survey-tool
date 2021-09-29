package org.pacific_emis.surveys.accreditation_core.interactors;

import android.annotation.SuppressLint;

import org.pacific_emis.surveys.accreditation_core.data.data_source.AccreditationDataSource;
import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.data.model.Answer;
import org.pacific_emis.surveys.accreditation_core.data.model.AnswerState;
import org.pacific_emis.surveys.accreditation_core.data.model.ObservationInfo;
import org.pacific_emis.surveys.accreditation_core.data.model.ObservationLogRecord;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableAccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableAnswer;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableCategory;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableCriteria;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationInfo;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationLogRecord;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableStandard;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableSubCriteria;
import org.pacific_emis.surveys.core.data.model.Subject;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.model.SurveyState;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class AccreditationSurveyInteractorImpl implements AccreditationSurveyInteractor {

    private final AccreditationDataSource accreditationDataSource;
    private final BehaviorSubject<Survey> surveyBehaviorSubject = BehaviorSubject.create();
    private final PublishSubject<MutableCategory> categoryPublishSubject = PublishSubject.create();
    private final PublishSubject<MutableStandard> standardPublishSubject = PublishSubject.create();
    private final PublishSubject<MutableCriteria> criteriaPublishSubject = PublishSubject.create();

    private MutableAccreditationSurvey survey;
    private long categoryId;
    private long standardId;
    private long criteriaId;
    private long subCriteriaId;

    public AccreditationSurveyInteractorImpl(AccreditationDataSource accreditationDataSource) {
        this.accreditationDataSource = accreditationDataSource;
    }

    @Override
    public Single<List<Survey>> getAllSurveys(AppRegion appRegion) {
        return accreditationDataSource.loadAllSurveys(appRegion)
                .flatMapObservable(Observable::fromIterable)
                .map(survey -> {
                    MutableAccreditationSurvey mutableSurvey = new MutableAccreditationSurvey((AccreditationSurvey) survey);
                    initProgress(mutableSurvey);
                    return mutableSurvey;
                })
                .toList()
                .map(ArrayList::new);
    }

    @Override
    public void setCurrentSurvey(Survey survey) {
        setCurrentSurvey(survey, false);
    }

    @Override
    public void setCurrentSurvey(Survey survey, boolean shouldFetchProgress) {
        this.survey = (MutableAccreditationSurvey) survey;
        if (shouldFetchProgress) {
            initProgress(this.survey);
        }
        // fill initial survey item to BehaviorSubject
        surveyBehaviorSubject.onNext(this.survey);
    }

    @Override
    public Survey getCurrentSurvey() {
        return survey;
    }

    private void initProgress(MutableAccreditationSurvey survey) {
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
    public Single<MutableCategory> requestCategory(long categoryId) {
        return requestCategories()
                .flatMapObservable(Observable::fromIterable)
                .filter(cat -> cat.getId() == categoryId)
                .firstOrError();
    }

    @Override
    public Single<List<MutableCriteria>> requestCriterias(long categoryId, long standardId) {
        return requestStandards(categoryId)
                .flatMapObservable(Observable::fromIterable)
                .filter(it -> it.getId() == standardId)
                .firstOrError()
                .map(MutableStandard::getCriterias);
    }

    @SuppressLint("CheckResult")
    @Override
    public Completable updateAnswer(Answer answer, long categoryId, long standardId, long criteriaId, long subCriteriaId) {
        return accreditationDataSource.updateAnswer(answer)
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
        markSurveyAsCompletedIfNeed();
        surveyBehaviorSubject.onNext(survey);
    }

    private void markSurveyAsCompletedIfNeed() {
        if (survey.getState() == SurveyState.NOT_COMPLETED && survey.getProgress().isFinished()) {
            survey.setCompleteDate(new Date());
            survey.setState(SurveyState.COMPLETED);
            accreditationDataSource.updateSurvey(survey);
        }
        if (!survey.getProgress().isFinished()) {
            if (survey.getState() == SurveyState.COMPLETED) {
                survey.setCompleteDate(null);
                survey.setState(SurveyState.NOT_COMPLETED);
                accreditationDataSource.updateSurvey(survey);
            } else {
                survey.setCompleteDate(null);
                survey.setState(SurveyState.NOT_COMPLETED);
            }
        }
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
    public Observable<Survey> getSurveyProgressObservable() {
        return surveyBehaviorSubject;
    }

    @Override
    public Observable<MutableCategory> getCategoryProgressObservable() {
        return categoryPublishSubject;
    }

    @Override
    public Observable<MutableStandard> getStandardProgressObservable() {
        return standardPublishSubject;
    }

    @Override
    public Observable<MutableCriteria> getCriteriaProgressObservable() {
        return criteriaPublishSubject;
    }

    @Override
    public Completable updateAnswer(Answer answer) {
        return updateAnswer(answer, categoryId, standardId, criteriaId, subCriteriaId);
    }

    @Override
    public Completable updateClassroomObservationInfo(ObservationInfo info, long categoryId) {
        return requestCategory(categoryId)
                .doOnSuccess(mutableCategory -> mutableCategory.setObservationInfo(MutableObservationInfo.from(info)))
                .flatMapCompletable(mutableCategory -> accreditationDataSource.updateObservationInfo(info, categoryId));
    }

    @Override
    public void setCurrentCategoryId(long id) {
        categoryId = id;
    }

    @Override
    public void setCurrentStandardId(long id) {
        standardId = id;
    }

    @Override
    public void setCurrentCriteriaId(long id) {
        criteriaId = id;
    }

    @Override
    public void setCurrentSubCriteriaId(long id) {
        subCriteriaId = id;
    }

    @Override
    public long getCurrentCategoryId() {
        return categoryId;
    }

    @Override
    public long getCurrentStandardId() {
        return standardId;
    }

    @Override
    public long getCurrentCriteriaId() {
        return criteriaId;
    }

    @Override
    public long getCurrentSubCriteriaId() {
        return subCriteriaId;
    }

    @Override
    public Completable completeSurveyIfNeed() {
        return Completable.fromAction(this::markSurveyAsCompletedIfNeed);
    }

    @Override
    public Single<List<MutableObservationLogRecord>> requestLogRecords(long categoryId) {
        return accreditationDataSource.getLogRecordsForCategoryWithId(categoryId);
    }

    @Override
    public Single<MutableObservationLogRecord> createEmptyLogRecord(long categoryId) {
        return accreditationDataSource.createEmptyLogRecord(categoryId, new Date());
    }

    @Override
    public Completable updateObservationLogRecord(ObservationLogRecord record) {
        return accreditationDataSource.updateObservationLogRecord(record);
    }

    @Override
    public Completable deleteObservationLogRecord(long recordId) {
        return accreditationDataSource.deleteObservationLogRecord(recordId);
    }

    @Override
    public Single<List<Teacher>> loadTeachers(AppRegion appRegion) {
        return accreditationDataSource.loadTeachers(appRegion);
    }

    @Override
    public Single<List<Subject>> loadSubjects(AppRegion appRegion) {
        return accreditationDataSource.loadSubjects(appRegion);
    }
}
