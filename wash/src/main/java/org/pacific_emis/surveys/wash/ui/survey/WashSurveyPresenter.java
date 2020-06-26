package org.pacific_emis.surveys.wash.ui.survey;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import org.pacific_emis.surveys.core.domain.SurveyInteractor;
import org.pacific_emis.surveys.survey_core.di.SurveyCoreComponent;
import org.pacific_emis.surveys.survey_core.navigation.BuildableNavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.NavigationItem;
import org.pacific_emis.surveys.survey_core.ui.survey.SurveyPresenter;
import org.pacific_emis.surveys.wash.navigation.GroupNavigationItem;
import org.pacific_emis.surveys.wash.navigation.SubGroupNavigationItem;
import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableGroup;
import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableSubGroup;
import org.pacific_emis.surveys.wash_core.di.WashCoreComponent;
import org.pacific_emis.surveys.wash_core.interactors.WashSurveyInteractor;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class WashSurveyPresenter extends SurveyPresenter<WashSurveyView> {

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
                washSurveyInteractor.getSubGroupProgressObservable()
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
