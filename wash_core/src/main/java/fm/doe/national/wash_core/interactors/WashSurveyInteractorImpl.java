package fm.doe.national.wash_core.interactors;

import java.util.List;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.wash_core.data.data_source.WashDataSource;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public class WashSurveyInteractorImpl implements WashSurveyInteractor {

    private final WashDataSource washDataSource;
    private final PublishSubject<Survey> surveyPublishSubject = PublishSubject.create();
//    private final PublishSubject<MutableCategory> categoryPublishSubject = PublishSubject.create();
//    private final PublishSubject<MutableStandard> standardPublishSubject = PublishSubject.create();
//    private final PublishSubject<MutableCriteria> criteriaPublishSubject = PublishSubject.create();
//
//    private MutableAccreditationSurvey survey;

    public WashSurveyInteractorImpl(WashDataSource washDataSource) {
        this.washDataSource = washDataSource;
    }

    @Override
    public Single<List<Survey>> getAllSurveys() {
        return null;
//        return washDataSource.loadAllSurveys()
//                .flatMapObservable(Observable::fromIterable)
//                .map(survey -> {
//                    MutableAccreditationSurvey mutableSurvey = new MutableAccreditationSurvey((AccreditationSurvey) survey);
//                    initProgress(mutableSurvey);
//                    return mutableSurvey;
//                })
//                .toList()
//                .map(list -> new ArrayList<>(list));
    }

    @Override
    public void setCurrentSurvey(Survey survey) {
        setCurrentSurvey(survey, false);
    }

    @Override
    public void setCurrentSurvey(Survey survey, boolean shouldFetchProgress) {
//        this.survey = (MutableAccreditationSurvey) survey;
//        if (shouldFetchProgress) {
//            initProgress(this.survey);
//        }
    }

    @Override
    public Survey getCurrentSurvey() {
        return null;
//        return survey;
    }

//    private void initProgress(MutableAccreditationSurvey survey) {
//        for (MutableCategory category : survey.getCategories()) {
//            initProgress(category);
//            survey.getProgress().add(category.getProgress());
//        }
//    }
//
//    private void initProgress(MutableCategory category) {
//        for (MutableStandard subGroup : category.getStandards()) {
//            initProgress(subGroup);
//            category.getProgress().add(subGroup.getProgress());
//        }
//    }
//
//    private void initProgress(MutableStandard subGroup) {
//        for (MutableCriteria criteria : subGroup.getCriterias()) {
//            initProgress(criteria);
//            subGroup.getProgress().add(criteria.getProgress());
//        }
//    }
//
//    private void initProgress(MutableCriteria criteria) {
//        criteria.getProgress().total = criteria.getSubCriterias().size();
//        for (MutableSubCriteria question : criteria.getSubCriterias()) {
//            if (question.getAnswer().getState() != AnswerState.NOT_ANSWERED) {
//                criteria.getProgress().completed++;
//            }
//        }
//    }
//
//    @Override
//    public Single<List<MutableCategory>> requestCategories() {
//        return Single.fromCallable(() -> survey.getCategories());
//    }
//
//    @Override
//    public Single<List<MutableStandard>> requestStandards(long categoryId) {
//        return requestCategories()
//                .flatMapObservable(Observable::fromIterable)
//                .filter(cat -> cat.getId() == categoryId)
//                .firstOrError()
//                .map(MutableCategory::getStandards);
//    }
//
//    @Override
//    public Single<List<MutableCriteria>> requestCriterias(long categoryId, long standardId) {
//        return requestStandards(categoryId)
//                .flatMapObservable(Observable::fromIterable)
//                .filter(it -> it.getId() == standardId)
//                .firstOrError()
//                .map(MutableStandard::getCriterias);
//    }
//
//    @SuppressLint("CheckResult")
//    @Override
//    public Completable updateAnswer(Answer answer, long categoryId, long standardId, long criteriaId, long subCriteriaId) {
//        return washDataSource.updateAnswer(answer, subCriteriaId)
//                .flatMapCompletable(updatedAnswer -> Completable.fromAction(() -> notifyProgressChanged(
//                        new MutableAnswer(updatedAnswer),
//                        categoryId,
//                        standardId,
//                        criteriaId,
//                        subCriteriaId
//                )));
//    }
//
//    private void notifyProgressChanged(MutableAnswer answer,
//                                       long categoryId,
//                                       long standardId,
//                                       long criteriaId,
//                                       long subCriteriaId) {
//        int delta = 0;
//        for (MutableCategory category : survey.getCategories()) {
//            if (category.getId() == categoryId) {
//                delta = findProgressDeltaAndNotify(answer, category, standardId, criteriaId, subCriteriaId);
//                break;
//            }
//        }
//
//        survey.getProgress().completed += delta;
//        surveyPublishSubject.onNext(survey);
//    }
//
//    private int findProgressDeltaAndNotify(MutableAnswer answer,
//                                           MutableCategory category,
//                                           long standardId,
//                                           long criteriaId,
//                                           long subCriteriaId) {
//        int delta = 0;
//        for (MutableStandard subGroup : category.getStandards()) {
//            if (subGroup.getId() == standardId) {
//                delta = findProgressDeltaAndNotify(answer, subGroup, criteriaId, subCriteriaId);
//                break;
//            }
//        }
//        category.getProgress().completed += delta;
//        categoryPublishSubject.onNext(category);
//        return delta;
//    }
//
//    private int findProgressDeltaAndNotify(MutableAnswer answer,
//                                           MutableStandard subGroup,
//                                           long criteriaId,
//                                           long subCriteriaId) {
//        int delta = 0;
//        for (MutableCriteria criteria : subGroup.getCriterias()) {
//            if (criteria.getId() == criteriaId) {
//                delta = findProgressDeltaAndNotify(answer, criteria, subCriteriaId);
//                break;
//            }
//        }
//        subGroup.getProgress().completed += delta;
//        standardPublishSubject.onNext(subGroup);
//        return delta;
//    }
//
//    private int findProgressDeltaAndNotify(MutableAnswer answer,
//                                           MutableCriteria criteria,
//                                           long subCriteriaId) {
//        int oldCompleted = criteria.getProgress().completed;
//        int completed = 0;
//        for (MutableSubCriteria question : criteria.getSubCriterias()) {
//            if (question.getId() == subCriteriaId) {
//                question.setAnswer(answer);
//            }
//            if (question.getAnswer().getState() != AnswerState.NOT_ANSWERED) {
//                completed++;
//            }
//        }
//        criteria.getProgress().completed = completed;
//        criteriaPublishSubject.onNext(criteria);
//        return completed - oldCompleted;
//    }

    @Override
    public PublishSubject<Survey> getSurveyProgressSubject() {
        return surveyPublishSubject;
    }

//    @Override
//    public PublishSubject<MutableCategory> getCategoryProgressSubject() {
//        return categoryPublishSubject;
//    }
//
//    @Override
//    public PublishSubject<MutableStandard> getStandardProgressSubject() {
//        return standardPublishSubject;
//    }
//
//    @Override
//    public PublishSubject<MutableCriteria> getCriteriaProgressSubject() {
//        return criteriaPublishSubject;
//    }

}
