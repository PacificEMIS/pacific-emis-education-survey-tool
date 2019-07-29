package fm.doe.national.ui.screens.templates.wash;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.survey_core.navigation.NavigationItem;
import fm.doe.national.ui.screens.templates.SurveyTemplatePresenter;
import fm.doe.national.wash.navigation.GroupNavigationItem;
import fm.doe.national.wash.navigation.SubGroupNavigationItem;
import fm.doe.national.wash_core.data.model.mutable.MutableGroup;
import fm.doe.national.wash_core.data.model.mutable.MutableSubGroup;
import fm.doe.national.wash_core.interactors.WashSurveyInteractor;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class WashSurveyTemplatePresenter extends SurveyTemplatePresenter {

    private final WashSurveyInteractor washSurveyInteractor = MicronesiaApplication.getInjection()
            .getWashCoreComponent()
            .getWashSurveyInteractor();

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
                dataSource.getTemplateSurvey()
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
