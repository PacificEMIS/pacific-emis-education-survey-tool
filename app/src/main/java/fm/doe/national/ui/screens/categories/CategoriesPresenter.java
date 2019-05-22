package fm.doe.national.ui.screens.categories;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.data.model.Category;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class CategoriesPresenter extends BasePresenter<CategoriesView> {

    private final SurveyInteractor interactor = MicronesiaApplication.getAppComponent().getSurveyInteractor();

    @Override
    public void attachView(CategoriesView view) {
        super.attachView(view);
        loadPassing();
        loadCategories();
    }

    public void onCategoryClicked(Category category) {
        getViewState().navigateToStandardsScreen(category.getId());
    }

    private void loadPassing() {
        CategoriesView view = getViewState();
        Survey survey = interactor.getCurrentSurvey();
        view.setSchoolName(survey.getSchoolName());
        view.setSurveyDate(survey.getDate());
    }

    private void loadCategories() {
        addDisposable(interactor.requestCategories()
                .map(mutableCategories -> new ArrayList<Category>(mutableCategories))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(getViewState()::showCategories, this::handleError));
    }
}
