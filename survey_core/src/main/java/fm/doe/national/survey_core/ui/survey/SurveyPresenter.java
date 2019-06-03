package fm.doe.national.survey_core.ui.survey;

import com.omegar.mvp.InjectViewState;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.interactors.SurveyInteractor;
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

    protected final SurveyInteractor surveyInteractor;
    protected final SurveyNavigator surveyNavigator;

    public SurveyPresenter(CoreComponent coreComponent, SurveyCoreComponent surveyCoreComponent) {
        surveyInteractor = coreComponent.getSurveyInteractor();
        surveyNavigator = surveyCoreComponent.getSurveyNavigator();

        surveyNavigator.setViewState(getViewState());

        getViewState().setSchoolName(surveyInteractor.getCurrentSurvey().getSchoolName());
        loadNavigationItems();
        subscribeOnEditingEvents();
    }

    protected abstract Single<List<NavigationItem>> requestNavigationItems();
    protected abstract void subscribeOnEditingEvents();

    private void loadNavigationItems() {
        addDisposable(requestNavigationItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
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

    public void onNavigationItemPressed(BuildableNavigationItem item) {
        surveyNavigator.select(item);
    }
}
