package org.pacific_emis.surveys.report_core.ui.recommendations;

import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import org.pacific_emis.surveys.report_core.model.recommendations.Recommendation;
import org.pacific_emis.surveys.report_core.model.recommendations.StandardRecommendation;
import org.pacific_emis.surveys.survey_core.di.SurveyCoreComponent;
import org.pacific_emis.surveys.survey_core.navigation.survey_navigator.SurveyNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class RecommendationsPresenter extends BasePresenter<RecommendationsView> {

    private final ReportInteractor interactor;
    private final SurveyNavigator surveyNavigator;

    public RecommendationsPresenter(ReportInteractor interactor, SurveyCoreComponent surveyCoreComponent) {
        this.interactor = interactor;
        this.surveyNavigator = surveyCoreComponent.getSurveyNavigator();
        loadRecommendations();
    }

    private void loadRecommendations() {
        addDisposable(interactor.getRecommendationsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().setRecommendationsLoadingVisibility(true))
                .doFinally(() -> getViewState().setRecommendationsLoadingVisibility(false))
                .subscribe(getViewState()::setRecommendations, this::handleError));
    }

    public void onRecommendationPressed(Recommendation recommendation) {
        if (!(recommendation instanceof StandardRecommendation)) {
            throw new IllegalStateException();
        }

        surveyNavigator.select(((StandardRecommendation) recommendation).getObject());
    }

}
