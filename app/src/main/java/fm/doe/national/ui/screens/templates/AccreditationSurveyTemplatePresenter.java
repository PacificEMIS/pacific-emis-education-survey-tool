package fm.doe.national.ui.screens.templates;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.accreditation.ui.navigation.concrete.CategoryNavigationItem;
import fm.doe.national.accreditation.ui.navigation.concrete.ReportNavigationItem;
import fm.doe.national.accreditation.ui.navigation.concrete.ReportTitleNavigationItem;
import fm.doe.national.accreditation.ui.navigation.concrete.StandardNavigationItem;
import fm.doe.national.accreditation_core.data.model.mutable.MutableCategory;
import fm.doe.national.accreditation_core.data.model.mutable.MutableStandard;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.accreditation_core.interactors.AccreditationSurveyInteractor;
import fm.doe.national.survey_core.navigation.BuildableNavigationItem;
import fm.doe.national.survey_core.navigation.NavigationItem;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AccreditationSurveyTemplatePresenter extends SurveyTemplatePresenter {

    private final AccreditationSurveyInteractor accreditationSurveyInteractor;

    public AccreditationSurveyTemplatePresenter(AccreditationCoreComponent accreditationCoreComponent) {
        this.accreditationSurveyInteractor = accreditationCoreComponent.getAccreditationSurveyInteractor();
    }

    @Override
    protected void loadItems() {
        addDisposable(
                accreditationSurveyInteractor.requestCategories()
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
                .subscribe(getViewState()::setItems, this::handleError)
        );
    }

    @Override
    protected void onLoadPressed() {

    }
}
