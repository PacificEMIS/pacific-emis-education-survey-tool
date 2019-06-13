package fm.doe.national.wash.ui;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.core.domain.SurveyInteractor;
import fm.doe.national.survey_core.di.SurveyCoreComponent;
import fm.doe.national.survey_core.navigation.BuildableNavigationItem;
import fm.doe.national.survey_core.navigation.NavigationItem;
import fm.doe.national.survey_core.ui.survey.SurveyPresenter;
import fm.doe.national.wash.navigation.GroupNavigationItem;
import fm.doe.national.wash.navigation.SubGroupNavigationItem;
import fm.doe.national.wash_core.data.model.mutable.MutableGroup;
import fm.doe.national.wash_core.data.model.mutable.MutableSubGroup;
import fm.doe.national.wash_core.di.WashCoreComponent;
import fm.doe.national.wash_core.interactors.WashSurveyInteractor;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class WashSurveyPresenter extends SurveyPresenter {

    private final WashSurveyInteractor washSurveyInteractor;

    public WashSurveyPresenter(WashCoreComponent washCoreComponent, SurveyCoreComponent surveyCoreComponent) {
        super(surveyCoreComponent);
        washSurveyInteractor = washCoreComponent.getWashSurveyInteractor();
        onInit();
    }

    @Override
    protected SurveyInteractor getSurveyInteractor() {
        return washSurveyInteractor;
    }

    @Override
    protected void subscribeOnEditingEvents() {
        addDisposable(
                washSurveyInteractor.getSubGroupProgressSubject()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(updatedSubGroup ->
                                        getViewState().updateQuestionsGroupProgress(
                                                updatedSubGroup.getId(),
                                                updatedSubGroup.getProgress()
                                        ),
                                this::handleError)
        );
    }

    @Override
    protected Single<List<NavigationItem>> requestNavigationItems() {
        return washSurveyInteractor.requestGroups()
                .flatMap(groups -> Single.fromCallable(() -> {
                    List<NavigationItem> navigationItems = new ArrayList<>();
                    BuildableNavigationItem prevBuildableNavigationItem = null;

                    for (MutableGroup group : groups) {
                        navigationItems.add(new GroupNavigationItem(group));

                        if (group.getSubGroups() != null) {
                            for (MutableSubGroup subGroup : group.getSubGroups()) {
                                SubGroupNavigationItem subGroupNavigationItem = new SubGroupNavigationItem(group, subGroup);
                                subGroupNavigationItem.setPreviousItem(prevBuildableNavigationItem);

                                if (prevBuildableNavigationItem != null) {
                                    prevBuildableNavigationItem.setNextItem(subGroupNavigationItem);
                                }

                                prevBuildableNavigationItem = subGroupNavigationItem;
                                navigationItems.add(subGroupNavigationItem);
                            }
                        }
                    }

                    return navigationItems;
                }));
    }

    @Override
    protected String getSchoolName() {
        return washSurveyInteractor.getCurrentSurvey().getSchoolName();
    }
}
