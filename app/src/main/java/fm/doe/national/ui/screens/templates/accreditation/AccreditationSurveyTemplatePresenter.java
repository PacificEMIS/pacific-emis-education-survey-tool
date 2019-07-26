package fm.doe.national.ui.screens.templates.accreditation;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.R;
import fm.doe.national.accreditation.ui.navigation.concrete.CategoryNavigationItem;
import fm.doe.national.accreditation.ui.navigation.concrete.StandardNavigationItem;
import fm.doe.national.accreditation_core.data.data_source.AccreditationDataSource;
import fm.doe.national.accreditation_core.data.model.mutable.MutableCategory;
import fm.doe.national.accreditation_core.data.model.mutable.MutableStandard;
import fm.doe.national.accreditation_core.interactors.AccreditationSurveyInteractor;
import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.survey_core.navigation.BuildableNavigationItem;
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


    private final AccreditationDataSource dataSource = MicronesiaApplication.getInjection()
            .getAccreditationCoreComponent()
            .getDataSource();

    public AccreditationSurveyTemplatePresenter() {
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
                            return navigationItems;
                        }))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::setItems, this::handleError)
        );
    }

    @Override
    protected void onLoadPressed() {
        getViewState().showToast(Text.from(R.string.coming_soon));
    }
}
