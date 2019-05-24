package fm.doe.national.fcm_report.domain;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.model.Category;
import fm.doe.national.core.data.model.Criteria;
import fm.doe.national.core.data.model.Standard;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.fcm_report.data.model.AccreditationForm;
import fm.doe.national.fcm_report.data.model.SchoolAccreditationLevel;
import fm.doe.national.report_core.domain.BaseReportInteractor;
import fm.doe.national.report_core.domain.ReportLevel;
import fm.doe.national.report_core.model.Level;
import fm.doe.national.report_core.model.SummaryViewData;
import fm.doe.national.report_core.ui.summary_header.SummaryHeaderView;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class FcmReportInteractorImpl extends BaseReportInteractor implements FcmReportInteractor {

    private static final SchoolAccreditationLevel EMPTY_LEVELS = SchoolAccreditationLevel.empty();

    private final BehaviorSubject<SchoolAccreditationLevel> levelSubject =
            BehaviorSubject.createDefault(EMPTY_LEVELS);

    @Override
    public void requestReports(Survey survey) {
        super.requestReports(survey);
        requestLevelReport(survey);
    }

    private void requestLevelReport(Survey survey) {
        Schedulers.computation().scheduleDirect(() -> {
            List<AccreditationForm.Builder> formBuilders = new ArrayList<>();

            for (Category category : survey.getCategories()) {
                AccreditationForm.Builder formBuilder = formBuilders.stream()
                        .filter(it -> it.getForm() == category.getEvaluationForm())
                        .findFirst()
                        .orElseGet(() -> {
                            AccreditationForm.Builder builder = new AccreditationForm.Builder()
                                    .setForm(category.getEvaluationForm());
                            formBuilders.add(builder);
                            return builder;
                        });

                for (Standard standard : category.getStandards()) {
                    int totalByStandard = 0;
                    int totalQuestions = 0;

                    for (Criteria criteria : standard.getCriterias()) {
                        SummaryViewData.CriteriaSummaryViewData data = createCriteriaSummaryViewData(criteria);
                        totalByStandard += data.getTotal();
                        totalQuestions += data.getAnswerStates().length;
                    }

                    formBuilder
                            .addObtainedScore(totalByStandard)
                            .addQuestionsCount(totalQuestions);
                }

            }

            levelSubject.onNext(new SchoolAccreditationLevel(formBuilders.stream()
                    .map(AccreditationForm.Builder::build).collect(Collectors.toList()))
            );
        });
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
    public Subject<SchoolAccreditationLevel> getLevelSubject() {
        return levelSubject;
    }

    @Override
    protected void requestHeader(Survey survey) {
        Schedulers.computation().scheduleDirect(() -> headerSubject.onNext(
                new SummaryHeaderView.Item(
                        survey.getSchoolId(),
                        survey.getSchoolName(),
                        survey.getDate(),
                        null, // TODO: not implemented
                        Arrays.asList(ReportLevel.values())
                )
        ));
    }
}
