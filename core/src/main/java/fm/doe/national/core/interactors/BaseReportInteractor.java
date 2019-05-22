package fm.doe.national.core.interactors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fm.doe.national.core.data.model.AnswerState;
import fm.doe.national.core.data.model.Category;
import fm.doe.national.core.data.model.Criteria;
import fm.doe.national.core.data.model.Level;
import fm.doe.national.core.data.model.Standard;
import fm.doe.national.core.data.model.SubCriteria;
import fm.doe.national.core.data.model.SummaryViewData;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.model.recommendations.CategoryRecommendation;
import fm.doe.national.core.data.model.recommendations.CriteriaRecommendation;
import fm.doe.national.core.data.model.recommendations.Recommendation;
import fm.doe.national.core.data.model.recommendations.StandardRecommendation;
import fm.doe.national.core.data.model.recommendations.SubCriteriaRecommendation;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public abstract class BaseReportInteractor implements ReportInteractor {

    private static final List<Recommendation> EMPTY_RECOMMENDATIONS = Collections.emptyList();
    private static final List<SummaryViewData> EMPTY_SUMMARY = Collections.emptyList();

    private final BehaviorSubject<List<Recommendation>> recommendationsSubject =
            BehaviorSubject.createDefault(EMPTY_RECOMMENDATIONS);

    private final BehaviorSubject<List<SummaryViewData>> summarySubject =
            BehaviorSubject.createDefault(EMPTY_SUMMARY);

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
    public void requestReports(Survey survey) {
        clearSubjectsHistory();
        requestSummary(survey);
        requestRecommendationsReport(survey);
    }

    private void clearSubjectsHistory() {
        recommendationsSubject.onNext(EMPTY_RECOMMENDATIONS);
        summarySubject.onNext(EMPTY_SUMMARY);
    }

    private void requestSummary(Survey survey) {
        Schedulers.computation().scheduleDirect(() -> {
            List<SummaryViewData> summaryViewDataList = new ArrayList<>();
            for (Category category : survey.getCategories()) {
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

    private SummaryViewData.CriteriaSummaryViewData createCriteriaSummaryViewData(Criteria criteria) {
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

    private void requestRecommendationsReport(Survey survey) {
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

}
