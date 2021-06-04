package org.pacific_emis.surveys.survey_core.ui.survey;

import java.util.List;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.core.domain.SurveyInteractor;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.survey_core.di.SurveyCoreComponent;
import org.pacific_emis.surveys.survey_core.navigation.BuildableNavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.NavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.ProgressablePrefixedBuildableNavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.survey_navigator.SurveyNavigator;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class SurveyPresenter<T extends SurveyView> extends BasePresenter<T> {

    protected final SurveyNavigator surveyNavigator;

    public SurveyPresenter(SurveyCoreComponent surveyCoreComponent) {
        surveyNavigator = surveyCoreComponent.getSurveyNavigator();
        surveyNavigator.setViewState(getViewState());
    }

    // Call this one in the end of inherited constructor
    protected void onInit() {
        getViewState().setSchoolName(getSchoolName());
        loadNavigationItems();
        subscribeOnEditingEvents();
        subscribeToSurveyProgress();
    }

    protected abstract Single<List<NavigationItem>> requestNavigationItems();
    protected abstract void subscribeOnEditingEvents();
    protected abstract String getSchoolName();
    protected abstract SurveyInteractor getSurveyInteractor();

    private void loadNavigationItems() {
        addDisposable(requestNavigationItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    surveyNavigator.setNavigation(items);
                    getViewState().setNavigationItems(items);
                    selectFirstNavigationItem(items);
                }, this::handleError));
    }

    private void selectFirstNavigationItem(List<NavigationItem> items) {
        List<ProgressablePrefixedBuildableNavigationItem> progressableItems = items.parallelStream()
                .filter(item -> item instanceof ProgressablePrefixedBuildableNavigationItem)
                .map(item -> (ProgressablePrefixedBuildableNavigationItem) item)
                .collect(Collectors.toList());

        if (progressableItems.isEmpty()) {
            return;
        }

        ProgressablePrefixedBuildableNavigationItem itemToSelect = progressableItems.parallelStream()
                .filter(item -> item.getProgress().getCompleted() != item.getProgress().getTotal())
                .findFirst()
                .orElse(progressableItems.get(0));

        surveyNavigator.select(itemToSelect);
    }

    private void subscribeToSurveyProgress() {
        addDisposable(getSurveyInteractor().getSurveyProgressObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(survey -> getViewState().setReportEnabled(survey.getProgress().isFinished()), this::handleError));
    }

    public void onNavigationItemPressed(BuildableNavigationItem item) {
        surveyNavigator.select(item);
    }
}
