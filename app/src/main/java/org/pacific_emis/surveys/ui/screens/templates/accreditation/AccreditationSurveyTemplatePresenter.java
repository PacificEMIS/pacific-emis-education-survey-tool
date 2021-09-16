package org.pacific_emis.surveys.ui.screens.templates.accreditation;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import org.pacific_emis.surveys.accreditation.ui.navigation.concrete.CategoryNavigationItem;
import org.pacific_emis.surveys.accreditation.ui.navigation.concrete.StandardNavigationItem;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableCategory;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableStandard;
import org.pacific_emis.surveys.accreditation_core.interactors.AccreditationSurveyInteractor;
import org.pacific_emis.surveys.app_support.MicronesiaApplication;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.survey_core.navigation.NavigationItem;
import org.pacific_emis.surveys.ui.screens.templates.SurveyTemplatePresenter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class AccreditationSurveyTemplatePresenter extends SurveyTemplatePresenter {

    private final AccreditationSurveyInteractor accreditationSurveyInteractor = MicronesiaApplication.getInjection()
            .getAccreditationCoreComponent()
            .getAccreditationSurveyInteractor();

    private final LocalSettings localSettings = MicronesiaApplication.getInjection().getCoreComponent().getLocalSettings();

    public AccreditationSurveyTemplatePresenter() {
        super(
                MicronesiaApplication.getInjection().getAccreditationCoreComponent().getDataSource(),
                MicronesiaApplication.getInjection().getAccreditationCoreComponent().getSurveyParser()
        );
        loadItems();
    }

    @Override
    protected void loadItems() {
        addDisposable(
                dataSource.getTemplateSurvey(localSettings.getCurrentAppRegion())
                        .flatMap(survey -> {
                            accreditationSurveyInteractor.setCurrentSurvey(survey);
                            return accreditationSurveyInteractor.requestCategories();
                        })
                        .flatMap(categories -> Single.fromCallable(() -> {
                            List<NavigationItem> navigationItems = new ArrayList<>();

                            for (MutableCategory category : categories) {
                                navigationItems.add(new CategoryNavigationItem(category));

                                for (MutableStandard standard : category.getStandards()) {
                                    StandardNavigationItem standardItem = new StandardNavigationItem(category, standard);
                                    navigationItems.add(standardItem);
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
