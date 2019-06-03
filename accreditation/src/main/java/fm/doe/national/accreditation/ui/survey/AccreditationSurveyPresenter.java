package fm.doe.national.accreditation.ui.survey;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.accreditation.ui.navigation.concrete.CategoryNavigationItem;
import fm.doe.national.accreditation.ui.navigation.concrete.ReportNavigationItem;
import fm.doe.national.accreditation.ui.navigation.concrete.ReportTitleNavigationItem;
import fm.doe.national.accreditation.ui.navigation.concrete.StandardNavigationItem;
import fm.doe.national.core.data.model.mutable.MutableCategory;
import fm.doe.national.core.data.model.mutable.MutableStandard;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.survey_core.di.SurveyCoreComponent;
import fm.doe.national.survey_core.navigation.BuildableNavigationItem;
import fm.doe.national.survey_core.navigation.NavigationItem;
import fm.doe.national.survey_core.ui.survey.SurveyPresenter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class AccreditationSurveyPresenter extends SurveyPresenter {

    public AccreditationSurveyPresenter(CoreComponent coreComponent, SurveyCoreComponent surveyCoreComponent) {
        super(coreComponent, surveyCoreComponent);
    }

    @Override
    protected void subscribeOnEditingEvents() {
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

    @Override
    protected Single<List<NavigationItem>> requestNavigationItems() {
        return surveyInteractor.requestCategories()
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
                }));
    }
}
