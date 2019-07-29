package fm.doe.national.ui.screens.templates.accreditation;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.accreditation.ui.navigation.concrete.CategoryNavigationItem;
import fm.doe.national.accreditation.ui.navigation.concrete.StandardNavigationItem;
import fm.doe.national.accreditation_core.data.model.mutable.MutableCategory;
import fm.doe.national.accreditation_core.data.model.mutable.MutableStandard;
import fm.doe.national.accreditation_core.interactors.AccreditationSurveyInteractor;
import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.survey_core.navigation.NavigationItem;
import fm.doe.national.ui.screens.templates.SurveyTemplatePresenter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class AccreditationSurveyTemplatePresenter extends SurveyTemplatePresenter {

    private final AccreditationSurveyInteractor accreditationSurveyInteractor = MicronesiaApplication.getInjection()
            .getAccreditationCoreComponent()
            .getAccreditationSurveyInteractor();

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
                dataSource.getTemplateSurvey()
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
