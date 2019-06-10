package fm.doe.national.survey_core.ui.survey;

import com.omegar.mvp.InjectViewState;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.domain.SurveyInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.survey_core.di.SurveyCoreComponent;
import fm.doe.national.survey_core.navigation.BuildableNavigationItem;
import fm.doe.national.survey_core.navigation.NavigationItem;
import fm.doe.national.survey_core.navigation.ProgressablePrefixedBuildableNavigationItem;
import fm.doe.national.survey_core.navigation.survey_navigator.SurveyNavigator;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public abstract class SurveyPresenter extends BasePresenter<SurveyView> {

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
        addDisposable(getSurveyInteractor().getSurveyProgressSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(survey -> getViewState().setReportEnabled(survey.getProgress().isFinished()), this::handleError));

    }

    public void onNavigationItemPressed(BuildableNavigationItem item) {
        surveyNavigator.select(item);
    }
}
