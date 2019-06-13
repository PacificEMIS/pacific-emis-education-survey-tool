package fm.doe.national.report_core.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public abstract class BaseReportInteractor implements ReportInteractor {

    private static final List<Recommendation> EMPTY_RECOMMENDATIONS = Collections.emptyList();
    private static final List<SummaryViewData> EMPTY_SUMMARY = Collections.emptyList();
    private static final LevelLegendView.Item EMPTY_HEADER = LevelLegendView.Item.empty();
    private static final int CLASSROOM_OBSERVATIONS_TO_TRIGGER_FILTER = 2;

    private final BehaviorSubject<List<Recommendation>> recommendationsSubject =
            BehaviorSubject.createDefault(EMPTY_RECOMMENDATIONS);

    private final BehaviorSubject<List<SummaryViewData>> summarySubject =
            BehaviorSubject.createDefault(EMPTY_SUMMARY);

    protected final BehaviorSubject<LevelLegendView.Item> headerSubject =
            BehaviorSubject.createDefault(EMPTY_HEADER);

    protected abstract Level createLevel(int completed, int total);

    @Override
    public Subject<List<Recommendation>> getRecommendationsSubject() {
        return recommendationsSubject;
    }

    @Override
    public Subject<List<SummaryViewData>> getSummarySubject() {
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
        recommendationsSubject.onNext(EMPTY_RECOMMENDATIONS);
        summarySubject.onNext(EMPTY_SUMMARY);
        headerSubject.onNext(EMPTY_HEADER);
    }

    protected AccreditationSurvey getSurveyWithWorstClassroomObservation(AccreditationSurvey survey) {
        MutableAccreditationSurvey otherSurvey = new MutableAccreditationSurvey(survey);

        List<MutableCategory> classroomObservations = otherSurvey.getCategories().stream()
                .filter(cat -> cat.getEvaluationForm() == EvaluationForm.CLASSROOM_OBSERVATION)
                .collect(Collectors.toList());

        List<MutableCategory> otherCategories =otherSurvey.getCategories().stream()
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
        Schedulers.computation().scheduleDirect(() -> headerSubject.onNext(
                new LevelLegendView.Item(
                        survey.getSchoolId(),
                        survey.getSchoolName(),
                        survey.getDate(),
                        null, // TODO: not implemented
                        Arrays.asList(ReportLevel.values())
                )
        ));
    }

    @Override
    public Subject<LevelLegendView.Item> getHeaderItemSubject() {
        return headerSubject;
    }
}
