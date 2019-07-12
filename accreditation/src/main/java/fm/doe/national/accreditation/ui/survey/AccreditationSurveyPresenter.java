package fm.doe.national.accreditation.ui.survey;

import com.omegar.mvp.InjectViewState;

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
import fm.doe.national.core.domain.SurveyInteractor;
import fm.doe.national.survey_core.di.SurveyCoreComponent;
import fm.doe.national.survey_core.navigation.BuildableNavigationItem;
import fm.doe.national.survey_core.navigation.NavigationItem;
import fm.doe.national.survey_core.ui.survey.SurveyPresenter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class AccreditationSurveyPresenter extends SurveyPresenter {

    private final AccreditationSurveyInteractor accreditationSurveyInteractor;

    public AccreditationSurveyPresenter(AccreditationCoreComponent accreditationCoreComponent, SurveyCoreComponent surveyCoreComponent) {
        super(surveyCoreComponent);
        accreditationSurveyInteractor = accreditationCoreComponent.getAccreditationSurveyInteractor();
        onInit();
    }

    @Override
    protected SurveyInteractor getSurveyInteractor() {
        return accreditationSurveyInteractor;
    }

    @Override
    protected void subscribeOnEditingEvents() {
        addDisposable(
                accreditationSurveyInteractor.getStandardProgressObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(updatedStandard ->
                                        getViewState().updateQuestionsGroupProgress(
                                                updatedStandard.getId(),
                                                updatedStandard.getProgress()
                                        ),
                                this::handleError)
        );
    }

    @Override
    protected Single<List<NavigationItem>> requestNavigationItems() {
        return accreditationSurveyInteractor.requestCategories()
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

    @Override
    protected String getSchoolName() {
        return accreditationSurveyInteractor.getCurrentSurvey().getSchoolName();
    }
}
