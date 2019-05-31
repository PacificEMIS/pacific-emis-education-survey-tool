package fm.doe.national.accreditation.ui.survey;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation.ui.navigation.BuildableNavigationItem;
import fm.doe.national.accreditation.ui.navigation.NavigationItem;
import fm.doe.national.accreditation.ui.navigation.ProgressablePrefixedBuildableNavigationItem;
import fm.doe.national.accreditation.ui.navigation.concrete.CategoryNavigationItem;
import fm.doe.national.accreditation.ui.navigation.concrete.ReportNavigationItem;
import fm.doe.national.accreditation.ui.navigation.concrete.StandardNavigationItem;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SurveyPresenter extends BasePresenter<SurveyView> {

    private final SurveyInteractor surveyInteractor;

    SurveyPresenter(CoreComponent coreComponent) {
        surveyInteractor = coreComponent.getSurveyInteractor();

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
                            categories.forEach(category -> {
                                navigationItems.add(new CategoryNavigationItem(category));
                                category.getStandards().forEach(standard ->
                                        navigationItems.add(new StandardNavigationItem(category, standard))
                                );
                            });
                            navigationItems.add(new ReportNavigationItem());
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
                .map(item -> (ProgressablePrefixedBuildableNavigationItem)item)
                .collect(Collectors.toList());

        if (progressableItems.isEmpty()) {
            return;
        }

        ProgressablePrefixedBuildableNavigationItem itemToSelect = progressableItems.parallelStream()
                .filter(item -> item.getProgress().getCompleted() != item.getProgress().getTotal())
                .findFirst()
                .orElse(progressableItems.get(0));

        onNavigationItemPressed(itemToSelect);
    }

    void onNavigationItemPressed(BuildableNavigationItem item) {
        if (item instanceof ProgressablePrefixedBuildableNavigationItem) {
            ProgressablePrefixedBuildableNavigationItem navigationItem = (ProgressablePrefixedBuildableNavigationItem) item;
            getViewState().setNavigationTitle(
                    navigationItem.getTitlePrefix(),
                    navigationItem.getTitle(),
                    navigationItem.getProgress()
            );
        } else {
            getViewState().setNavigationTitle(null, item.getTitle(), null);
        }
        getViewState().showNavigationItem(item);
    }
}
