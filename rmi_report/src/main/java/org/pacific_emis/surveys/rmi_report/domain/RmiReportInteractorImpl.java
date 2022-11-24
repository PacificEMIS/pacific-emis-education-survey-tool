package org.pacific_emis.surveys.rmi_report.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.data.model.AnswerState;
import org.pacific_emis.surveys.accreditation_core.data.model.Category;
import org.pacific_emis.surveys.accreditation_core.data.model.Criteria;
import org.pacific_emis.surveys.accreditation_core.data.model.EvaluationForm;
import org.pacific_emis.surveys.accreditation_core.data.model.Standard;
import org.pacific_emis.surveys.accreditation_core.data.model.SubCriteria;
import org.pacific_emis.surveys.core.data.exceptions.NotImplementedException;
import org.pacific_emis.surveys.report_core.domain.BaseReportInteractor;
import org.pacific_emis.surveys.rmi_report.model.SchoolAccreditationTallyLevel;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class RmiReportInteractorImpl extends BaseReportInteractor implements RmiReportInteractor {

    private BehaviorSubject<SchoolAccreditationTallyLevel> levelSubject = BehaviorSubject.create();

    @Override
    public void requestReports(AccreditationSurvey survey) {
        super.requestReports(survey);
        requestLevelReport(survey);
    }

    @Override
    protected void clearSubjectsHistory() {
        super.clearSubjectsHistory();
        levelSubject.onComplete();
        levelSubject = BehaviorSubject.create();
    }

    @Override
    protected RmiReportLevel createLevel(int completed, int total) {
        return RmiReportLevel.estimateLevel(completed, total);
    }

    @Override
    public Observable<SchoolAccreditationTallyLevel> getLevelObservable() {
        return levelSubject;
    }

    private void requestLevelReport(AccreditationSurvey survey) {
        Schedulers.computation().scheduleDirect(() -> {
            AccreditationSurvey flattenSurvey = getFlattenSurvey(survey);

            List<Category> seCategories = flattenSurvey.getCategories().stream()
                    .filter(it -> it.getEvaluationForm() == EvaluationForm.SCHOOL_EVALUATION)
                    .collect(Collectors.toList());

            Map<String, List<Category>> coCategoriesListByTitle = flattenSurvey.getCategories().stream()
                    .filter(it -> it.getEvaluationForm() == EvaluationForm.CLASSROOM_OBSERVATION)
                    .collect(Collectors.groupingBy(Category::getTitle));

            List<List<Category>> coCategoriesList = new ArrayList<>(coCategoriesListByTitle.values());

            int[] counts = new int[SchoolAccreditationTallyLevel.MAX_CRITERIA_SUM];

            // populate Tally scores from School Evaluation results
            for (Category category : seCategories) {
                for (Standard standard : category.getStandards()) {
                    for (Criteria criteria : standard.getCriterias()) {
                        int criteriaSum = getPositiveAnswersCount(criteria);
                        if (criteriaSum > 0 && criteriaSum <= SchoolAccreditationTallyLevel.MAX_CRITERIA_SUM) {
                            counts[criteriaSum - 1]++;
                        }
                    }
                }
            }

            // populate Tally scores from School Evaluation results
            for (List<Category> coCategories : coCategoriesList) {
                int answeredCount = 0;
                int questionsCount = 0;
                for (Category category : coCategories) {
                    for (Standard standard : category.getStandards()) {
                        for (Criteria criteria : standard.getCriterias()) {
                            answeredCount += getPositiveAnswersCount(criteria);
                            questionsCount += criteria.getSubCriterias().size();
                        }
                    }
                }

                RmiReportLevel categoryLevel = createLevel(answeredCount, questionsCount);
                int tallyValue = reportLevelToTallyValue(categoryLevel);

                if (tallyValue > 0 && tallyValue <= SchoolAccreditationTallyLevel.MAX_CRITERIA_SUM) {
                    counts[tallyValue - 1]++;
                }
            }

            levelSubject.onNext(new SchoolAccreditationTallyLevel(counts));
        });
    }

    private int reportLevelToTallyValue(RmiReportLevel level) {
        switch (level) {
            case LEVEL_1:
                return 1;
            case LEVEL_2:
                return 2;
            case LEVEL_3:
                return 3;
            case LEVEL_4:
                return 4;
            default:
                throw new NotImplementedException();
        }
    }

    private int getPositiveAnswersCount(Criteria criteria) {
        int criteriaSum = 0;
        for (SubCriteria subCriteria : criteria.getSubCriterias()) {
            if (subCriteria.getAnswer().getState() == AnswerState.POSITIVE) {
                criteriaSum++;
            }
        }
        return criteriaSum;
    }
}
