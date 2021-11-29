package org.pacific_emis.surveys.ui.screens.templates.wash;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import org.pacific_emis.surveys.app_support.MicronesiaApplication;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.survey_core.navigation.NavigationItem;
import org.pacific_emis.surveys.ui.screens.templates.SurveyTemplatePresenter;
import org.pacific_emis.surveys.wash.navigation.GroupNavigationItem;
import org.pacific_emis.surveys.wash.navigation.SubGroupNavigationItem;
import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableGroup;
import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableSubGroup;
import org.pacific_emis.surveys.wash_core.interactors.WashSurveyInteractor;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class WashSurveyTemplatePresenter extends SurveyTemplatePresenter {

    private final WashSurveyInteractor washSurveyInteractor = MicronesiaApplication.getInjection()
            .getWashCoreComponent()
            .getWashSurveyInteractor();

    private final LocalSettings localSettings = MicronesiaApplication.getInjection().getCoreComponent().getLocalSettings();

    public WashSurveyTemplatePresenter() {
        super(
                MicronesiaApplication.getInjection().getWashCoreComponent().getDataSource(),
                MicronesiaApplication.getInjection().getWashCoreComponent().getSurveyParser()
        );
        loadItems();
    }

    @Override
    protected void loadItems() {
        addDisposable(
                dataSource.getTemplateSurvey(localSettings.getCurrentAppRegion())
                        .flatMap(survey -> {
                            washSurveyInteractor.setCurrentSurvey(survey);
                            return washSurveyInteractor.requestGroups();
                        })
                        .flatMap(groups -> Single.fromCallable(() -> {
                            List<NavigationItem> navigationItems = new ArrayList<>();

                            for (MutableGroup group : groups) {
                                navigationItems.add(new GroupNavigationItem(group));

                                if (group.getSubGroups() != null) {
                                    for (MutableSubGroup subGroup : group.getSubGroups()) {
                                        SubGroupNavigationItem subGroupNavigationItem = new SubGroupNavigationItem(group, subGroup);
                                        navigationItems.add(subGroupNavigationItem);
                                    }
                                }
                            }

                            return navigationItems;
                        }))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::setItems, this::handleError)
        );
    }
}
