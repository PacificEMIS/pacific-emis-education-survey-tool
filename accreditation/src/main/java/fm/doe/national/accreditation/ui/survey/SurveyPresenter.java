package fm.doe.national.accreditation.ui.survey;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation.di.AccreditationComponent;
import fm.doe.national.accreditation.ui.navigation.BuildableNavigationItem;
import fm.doe.national.accreditation.ui.navigation.NavigationItem;
import fm.doe.national.accreditation.ui.navigation.ProgressablePrefixedBuildableNavigationItem;
import fm.doe.national.accreditation.ui.navigation.concrete.CategoryNavigationItem;
import fm.doe.national.accreditation.ui.navigation.concrete.ReportNavigationItem;
import fm.doe.national.accreditation.ui.navigation.concrete.ReportTitleNavigationItem;
import fm.doe.national.accreditation.ui.navigation.concrete.StandardNavigationItem;
import fm.doe.national.accreditation.ui.navigation.survey_navigator.SurveyNavigator;
import fm.doe.national.core.data.model.mutable.MutableCategory;
import fm.doe.national.core.data.model.mutable.MutableStandard;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SurveyPresenter extends BasePresenter<SurveyView> {

    private final SurveyInteractor surveyInteractor;
    private final SurveyNavigator surveyNavigator;

    SurveyPresenter(CoreComponent coreComponent, AccreditationComponent accreditationComponent) {
        surveyInteractor = coreComponent.getSurveyInteractor();
        surveyNavigator = accreditationComponent.getSurveyNavigator();

        surveyNavigator.setViewState(getViewState());

        getViewState().setSchoolName(surveyInteractor.getCurrentSurvey().getSchoolName());
        loadNavigationItems();
        subscribeOnEditingEvents();
    }

    private void subscribeOnEditingEvents() {
        addDisposable(
                surveyInteractor.getStandardProgressSubject()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(updatedStandard ->
                                        getViewState().updateStandardProgress(
                                                updatedStandard.getId(),
                                                updatedStandard.getProgress()
                                        ),
                                this::handleError)
        );
    }

    private void loadNavigationItems() {
        addDisposable(
                surveyInteractor.requestCategories()
                        .flatMap(categories -> Single.fromCallable(() -> {
                            List<NavigationItem> navigationItems = new ArrayList<>();
                            BuildableNavigationItem prevBuildableNavigationItem = null;

                            for (MutableCategory category : categories) {
                                navigationItems.add(new CategoryNavigationItem(category));

                                for (MutableStandard standard : category.getStandards()) {
                                    StandardNavigationItem standardItem = new StandardNavigationItem(category, standard);
                                    standardItem.setPreviousItem(prevBuildableNavigationItem);

                                    if (prevBuildableNavigationItem != null) {
                                        prevBuildableNavigationItem.setNextItem(standardItem);
                                    }

                                    prevBuildableNavigationItem = standardItem;
                                    navigationItems.add(standardItem);
                                }
                            }
                            navigationItems.add(new ReportTitleNavigationItem());
                            ReportNavigationItem reportNavigationItem = new ReportNavigationItem();

                            if (prevBuildableNavigationItem != null) {
                                prevBuildableNavigationItem.setNextItem(reportNavigationItem);
                            }

                            navigationItems.add(reportNavigationItem);
                            return navigationItems;
                        }))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(items -> {
                            getViewState().setNavigationItems(items);
                            selectFirstNavigationItem(items);
                        }, this::handleError)
        );
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

    void onNavigationItemPressed(BuildableNavigationItem item) {
        surveyNavigator.select(item);
    }
}
