package fm.doe.national.report_core.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.accreditation_core.data.model.AnswerState;
import fm.doe.national.accreditation_core.data.model.Category;
import fm.doe.national.accreditation_core.data.model.Criteria;
import fm.doe.national.accreditation_core.data.model.EvaluationForm;
import fm.doe.national.accreditation_core.data.model.Standard;
import fm.doe.national.accreditation_core.data.model.SubCriteria;
import fm.doe.national.accreditation_core.data.model.mutable.MutableAccreditationSurvey;
import fm.doe.national.accreditation_core.data.model.mutable.MutableCategory;
import fm.doe.national.report_core.model.Level;
import fm.doe.national.report_core.model.SummaryViewData;
import fm.doe.national.report_core.model.recommendations.CategoryRecommendation;
import fm.doe.national.report_core.model.recommendations.CriteriaRecommendation;
import fm.doe.national.report_core.model.recommendations.Recommendation;
import fm.doe.national.report_core.model.recommendations.StandardRecommendation;
import fm.doe.national.report_core.model.recommendations.SubCriteriaRecommendation;
import fm.doe.national.report_core.ui.level_legend.LevelLegendView;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public abstract class BaseReportInteractor implements ReportInteractor {

    private static final int CLASSROOM_OBSERVATIONS_TO_TRIGGER_FILTER = 2;

    private BehaviorSubject<List<Recommendation>> recommendationsSubject = BehaviorSubject.create();

    private BehaviorSubject<List<SummaryViewData>> summarySubject = BehaviorSubject.create();

    protected BehaviorSubject<LevelLegendView.Item> headerSubject = BehaviorSubject.create();

    protected abstract Level createLevel(int completed, int total);

    @Override
    public Observable<List<Recommendation>> getRecommendationsObservable() {
        return recommendationsSubject;
    }

    @Override
    public Observable<List<SummaryViewData>> getSummarySubjectObservable() {
        return summarySubject;
    }

    @Override
    public void requestReports(AccreditationSurvey survey) {
        clearSubjectsHistory();
        requestSummary(survey);
        requestRecommendationsReport(survey);
        requestHeader(survey);
    }

    protected void clearSubjectsHistory() {
        recommendationsSubject.onComplete();
        recommendationsSubject = BehaviorSubject.create();
        summarySubject.onComplete();
        summarySubject = BehaviorSubject.create();
        headerSubject.onComplete();
        headerSubject = BehaviorSubject.create();
    }

    protected AccreditationSurvey getSurveyWithWorstClassroomObservation(AccreditationSurvey survey) {
        MutableAccreditationSurvey otherSurvey = new MutableAccreditationSurvey(survey);

        List<MutableCategory> classroomObservations = otherSurvey.getCategories().stream()
                .filter(cat -> cat.getEvaluationForm() == EvaluationForm.CLASSROOM_OBSERVATION)
                .collect(Collectors.toList());

        List<MutableCategory> otherCategories = otherSurvey.getCategories().stream()
                .filter(cat -> cat.getEvaluationForm() != EvaluationForm.CLASSROOM_OBSERVATION)
                .collect(Collectors.toList());

        if (classroomObservations.size() == CLASSROOM_OBSERVATIONS_TO_TRIGGER_FILTER) {
            MutableCategory cat0 = classroomObservations.get(0);
            MutableCategory cat1 = classroomObservations.get(1);
            long sum0 = getCategorySum(cat0);
            long sum1 = getCategorySum(cat1);

            List<MutableCategory> resultCategories = new ArrayList<>(otherCategories);

            if (sum0 <= sum1) {
                resultCategories.add(cat0);
            } else {
                resultCategories.add(cat1);
            }

            otherSurvey.setCategories(resultCategories);
        }

        return otherSurvey;
    }

    private long getCategorySum(Category category) {
        return category.getStandards().stream()
                .flatMap(s -> s.getCriterias().stream())
                .flatMap(c -> c.getSubCriterias().stream())
                .filter(sc -> sc.getAnswer().getState() == AnswerState.POSITIVE)
                .count();
    }

    private void requestSummary(AccreditationSurvey survey) {
        Schedulers.computation().scheduleDirect(() -> {
            AccreditationSurvey clearedSurvey = getSurveyWithWorstClassroomObservation(survey);
            List<SummaryViewData> summaryViewDataList = new ArrayList<>();
            for (Category category : clearedSurvey.getCategories()) {
                for (Standard standard : category.getStandards()) {
                    List<SummaryViewData.CriteriaSummaryViewData> criteriaSummaryViewDataList = new ArrayList<>();
                    int totalByStandard = 0;
                    int totalQuestions = 0;

                    for (Criteria criteria : standard.getCriterias()) {
                        SummaryViewData.CriteriaSummaryViewData data = createCriteriaSummaryViewData(criteria);
                        totalByStandard += data.getTotal();
                        totalQuestions += data.getAnswerStates().length;
                        criteriaSummaryViewDataList.add(data);
                    }

                    summaryViewDataList.add(new SummaryViewData(
                            category,
                            standard,
                            totalByStandard,
                            criteriaSummaryViewDataList,
                            createLevel(totalByStandard, totalQuestions)
                    ));
                }
            }
            summarySubject.onNext(summaryViewDataList);
        });
    }

    protected SummaryViewData.CriteriaSummaryViewData createCriteriaSummaryViewData(Criteria criteria) {
        int totalByCriteria = 0;
        boolean[] positivesArray = new boolean[criteria.getSubCriterias().size()];

        for (int i = 0; i < criteria.getSubCriterias().size(); i++) {
            switch (criteria.getSubCriterias().get(i).getAnswer().getState()) {
                case POSITIVE:
                    totalByCriteria++;
                    positivesArray[i] = true;
                    break;
                default:
                    positivesArray[i] = false;
                    break;
            }
        }

        return new SummaryViewData.CriteriaSummaryViewData(criteria.getSuffix(), positivesArray, totalByCriteria);
    }

    private void requestRecommendationsReport(AccreditationSurvey survey) {
        Schedulers.computation().scheduleDirect(() -> {
            List<Recommendation> recommendations = generateCategoryRecommendations(survey.getCategories());
            recommendationsSubject.onNext(recommendations);
        });
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

    protected void requestHeader(AccreditationSurvey survey) {
        headerSubject.onNext(
                new LevelLegendView.Item(
                        survey.getSchoolId(),
                        survey.getSchoolName(),
                        survey.getSurveyDate(),
                        null, // TODO: not implemented
                        Arrays.asList(ReportLevel.values())
                )
        );
    }

    @Override
    public Observable<LevelLegendView.Item> getHeaderItemObservable() {
        return headerSubject;
    }
}
