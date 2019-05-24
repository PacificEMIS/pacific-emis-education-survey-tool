package fm.doe.national.rmi_report.domain;

import fm.doe.national.core.data.model.Category;
import fm.doe.national.core.data.model.Criteria;
import fm.doe.national.core.data.model.Standard;
import fm.doe.national.core.data.model.SubCriteria;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.report_core.domain.BaseReportInteractor;
import fm.doe.national.report_core.domain.ReportLevel;
import fm.doe.national.report_core.model.Level;
import fm.doe.national.rmi_report.model.SchoolAccreditationTallyLevel;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class RmiReportInteractorImpl extends BaseReportInteractor implements RmiReportInteractor {

    private static final SchoolAccreditationTallyLevel EMPTY_LEVELS = SchoolAccreditationTallyLevel.empty();

    private final BehaviorSubject<SchoolAccreditationTallyLevel> levelSubject = BehaviorSubject.createDefault(EMPTY_LEVELS);

    @Override
    public void requestReports(Survey survey) {
        super.requestReports(survey);
        requestLevelReport(survey);
    }

    @Override
    protected void clearSubjectsHistory() {
        super.clearSubjectsHistory();
        levelSubject.onNext(EMPTY_LEVELS);
    }

    @Override
    protected Level createLevel(int completed, int total) {
        return ReportLevel.estimateLevel(completed, total);
    }

    @Override
    public Subject<SchoolAccreditationTallyLevel> getLevelSubject() {
        return levelSubject;
    }

    private void requestLevelReport(Survey survey) {
        Schedulers.computation().scheduleDirect(() -> {
            int countOfOnes = 0;
            int countOfTwos = 0;
            int countOfThrees = 0;
            int countOfFourth = 0;
            int tallyScore = 0;
            for (Category category : survey.getCategories()) {
                for (Standard standard : category.getStandards()) {
                    for (Criteria criteria : standard.getCriterias()) {
                        int criteriaSum = 0;
                        for (SubCriteria subCriteria : criteria.getSubCriterias()) {
                            switch (subCriteria.getAnswer().getState()) {
                                case POSITIVE:
                                    criteriaSum++;
                            }
                        }
                        switch (criteriaSum) {
                            case 1:
                                countOfOnes++;
                                break;
                            case 2:
                                countOfTwos++;
                                break;
                            case 3:
                                countOfThrees++;
                            case 4:
                                countOfFourth++;
                        }
                        tallyScore += criteriaSum;
                    }
                }
            }
            levelSubject.onNext(
                    new SchoolAccreditationTallyLevel(
                            countOfOnes,
                            countOfTwos,
                            countOfThrees,
                            countOfFourth,
                            tallyScore
                    )
            );
        });
    }
}
