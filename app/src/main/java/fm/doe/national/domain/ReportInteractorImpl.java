package fm.doe.national.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fm.doe.national.app_support.utils.CollectionUtils;
import fm.doe.national.data.model.AnswerState;
import fm.doe.national.data.model.Category;
import fm.doe.national.data.model.Criteria;
import fm.doe.national.data.model.EvaluationForm;
import fm.doe.national.data.model.Standard;
import fm.doe.national.data.model.SubCriteria;
import fm.doe.national.data.model.Survey;
import fm.doe.national.data.model.recommendations.CategoryRecommendation;
import fm.doe.national.data.model.recommendations.CriteriaRecommendation;
import fm.doe.national.data.model.recommendations.Recommendation;
import fm.doe.national.data.model.recommendations.StandardRecommendation;
import fm.doe.national.data.model.recommendations.SubCriteriaRecommendation;
import fm.doe.national.ui.custom_views.summary_header.SummaryHeaderView;
import fm.doe.national.ui.screens.report.ReportLevel;
import fm.doe.national.ui.screens.report.levels.AccreditationForm;
import fm.doe.national.ui.screens.report.levels.SchoolAccreditationLevel;
import fm.doe.national.ui.screens.report.summary.SummaryViewData;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class ReportInteractorImpl implements ReportInteractor {

    private static final List<Recommendation> EMPTY_RECOMMENDATIONS = Collections.emptyList();
    private static final List<SummaryViewData> EMPTY_SUMMARY = Collections.emptyList();
    private static final SchoolAccreditationLevel EMPTY_LEVELS = SchoolAccreditationLevel.empty();
    private static final SummaryHeaderView.Item EMPTY_HEADER = SummaryHeaderView.Item.empty();

    private final BehaviorSubject<List<Recommendation>> recommendationsSubject =
            BehaviorSubject.createDefault(EMPTY_RECOMMENDATIONS);

    private final BehaviorSubject<SchoolAccreditationLevel> levelSubject =
            BehaviorSubject.createDefault(EMPTY_LEVELS);

    private final BehaviorSubject<SummaryHeaderView.Item> headerSubject =
            BehaviorSubject.createDefault(EMPTY_HEADER);

    private final BehaviorSubject<List<SummaryViewData>> summarySubject =
            BehaviorSubject.createDefault(EMPTY_SUMMARY);


    @Override
    public Subject<List<Recommendation>> getRecommendationsSubject() {
        return recommendationsSubject;
    }

    @Override
    public Subject<SchoolAccreditationLevel> getLevelSubject() {
        return levelSubject;
    }

    @Override
    public Subject<List<SummaryViewData>> getSummarySubject() {
        return summarySubject;
    }

    @Override
    public void requestReports(Survey survey) {
        clearSubjectsHistory();
        requestHeader(survey);
        requestSummaryAndLevelReports(survey);
        requestRecommendationsReport(survey);
    }

    private void clearSubjectsHistory() {
        recommendationsSubject.onNext(EMPTY_RECOMMENDATIONS);
        levelSubject.onNext(EMPTY_LEVELS);
        summarySubject.onNext(EMPTY_SUMMARY);
        headerSubject.onNext(EMPTY_HEADER);
    }

    private void requestHeader(Survey survey) {
        Schedulers.computation().scheduleDirect(() -> {
            headerSubject.onNext(
                    new SummaryHeaderView.Item(
                            survey.getSchoolId(),
                            survey.getSchoolName(),
                            survey.getDate(),
                            null, // TODO: not implemented
                            Arrays.asList(ReportLevel.values())
                            )
            );
        });
    }

    private void requestSummaryAndLevelReports(Survey survey) {
        Schedulers.computation().scheduleDirect(() -> {

            List<SummaryViewData> summaryViewDataList = new ArrayList<>();
            List<AccreditationForm.Builder> formBuilders = new ArrayList<>();

            for (Category category : survey.getCategories()) {
                AccreditationForm.Builder formBuilder = CollectionUtils.firstWhere(
                        formBuilders,
                        it -> it.getName() == category.getEvaluationForm().getName()
                );

                if (formBuilder == null) {
                    formBuilder = new AccreditationForm.Builder()
                            .setName(category.getEvaluationForm().getName())
                            .setMultiplier(calculateFormMultiplier(category.getEvaluationForm())); // TODO: this one is temp
                    formBuilders.add(formBuilder);
                }

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

                    formBuilder.addObtainedScore(totalByStandard);
                    summaryViewDataList.add(new SummaryViewData(
                            category,
                            standard,
                            totalByStandard,
                            totalQuestions,
                            criteriaSummaryViewDataList
                    ));
                }
            }

            summarySubject.onNext(summaryViewDataList);
            levelSubject.onNext(new SchoolAccreditationLevel(CollectionUtils.map(formBuilders, AccreditationForm.Builder::build)));
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

    private float calculateFormMultiplier(EvaluationForm evaluationForm) {
        // TODO: this is temp logic so change it when client answers
        switch (evaluationForm) {
            case CLASSROOM_OBSERVATION:
                return 0.1667f;
            case SCHOOL_EVALUATION:
                return 0.9375f;
        }
        return 0f;
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

    @Override
    public Subject<SummaryHeaderView.Item> getHeaderItemSubject() {
        return headerSubject;
    }
}
