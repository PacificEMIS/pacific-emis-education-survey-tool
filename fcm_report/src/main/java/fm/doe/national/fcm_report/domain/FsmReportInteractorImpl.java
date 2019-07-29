package fm.doe.national.fcm_report.domain;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.accreditation_core.data.model.Category;
import fm.doe.national.accreditation_core.data.model.Criteria;
import fm.doe.national.accreditation_core.data.model.Standard;
import fm.doe.national.fcm_report.data.model.AccreditationForm;
import fm.doe.national.fcm_report.data.model.SchoolAccreditationLevel;
import fm.doe.national.report_core.domain.BaseReportInteractor;
import fm.doe.national.report_core.domain.ReportLevel;
import fm.doe.national.report_core.model.Level;
import fm.doe.national.report_core.model.SummaryViewData;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class FsmReportInteractorImpl extends BaseReportInteractor implements FsmReportInteractor {

    private BehaviorSubject<SchoolAccreditationLevel> levelSubject = BehaviorSubject.create();

    @Override
    public void requestReports(AccreditationSurvey survey) {
        super.requestReports(survey);
        requestLevelReport(survey);
    }

    private void requestLevelReport(AccreditationSurvey survey) {
        Schedulers.computation().scheduleDirect(() -> {
            AccreditationSurvey clearedSurvey = getSurveyWithWorstClassroomObservation(survey);
            List<AccreditationForm.Builder> formBuilders = new ArrayList<>();

            for (Category category : clearedSurvey.getCategories()) {
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
        levelSubject.onComplete();
        levelSubject = BehaviorSubject.create();
    }

    @Override
    protected Level createLevel(int completed, int total) {
        return ReportLevel.estimateLevel(completed, total);
    }

    @Override
    public Observable<SchoolAccreditationLevel> getLevelObservable() {
        return levelSubject;
    }

}
