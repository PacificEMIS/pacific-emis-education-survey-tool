package fm.doe.national.accreditation.ui.survey;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.accreditation.ui.survey.navigation.BuildableNavigationItem;
import fm.doe.national.accreditation.ui.survey.navigation.ProgressablePrefixedBuildableNavigationItem;
import fm.doe.national.accreditation.ui.survey.navigation.concrete.CategoryNavigationItem;
import fm.doe.national.accreditation.ui.survey.navigation.concrete.ReportNavigationItem;
import fm.doe.national.accreditation.ui.survey.navigation.concrete.StandardNavigationItem;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.accreditation.ui.survey.navigation.NavigationItem;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SurveyPresenter extends BasePresenter<SurveyView> {

    private final SurveyInteractor surveyInteractor;

    SurveyPresenter(CoreComponent coreComponent) {
        surveyInteractor = coreComponent.getSurveyInteractor();

        getViewState().setSchoolName(surveyInteractor.getCurrentSurvey().getSchoolName());
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
                        .subscribe(getViewState()::setNavigationItems, this::handleError)
        );
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
