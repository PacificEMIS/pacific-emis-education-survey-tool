package fm.doe.national.ui.screens.categories;

import com.arellomobile.mvp.InjectViewState;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class CategoriesPresenter extends BasePresenter<CategoriesView> {

    private final long passingId;

    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();

    public CategoriesPresenter(long passingId) {
        this.passingId = passingId;
    }

    @Override
    public void attachView(CategoriesView view) {
        super.attachView(view);
        loadPassing();
    }

    public void onCategoryClicked(GroupStandard group) {
        getViewState().navigateToStandardsScreen(passingId, group.getId());
    }

    private void loadPassing() {
        addDisposable(dataSource.requestSchoolAccreditationPassing(passingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(passing -> {
                    CategoriesView view = getViewState();
                    view.setSurveyYear(passing.getYear());
                    view.setSchoolName(passing.getSchool().getName());
                }, this::handleError));

        addDisposable(dataSource.requestGroupStandards(passingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(groupStandards -> getViewState().showGroupStandards(groupStandards), this::handleError));
    }

}
