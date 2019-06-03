package fm.doe.national.report_core.ui.recommendations;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.report_core.model.recommendations.Recommendation;
import fm.doe.national.report_core.model.recommendations.StandardRecommendation;
import fm.doe.national.survey_core.di.SurveyCoreComponent;
import fm.doe.national.survey_core.navigation.survey_navigator.SurveyNavigator;
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
        addDisposable(interactor.getRecommendationsSubject()
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
