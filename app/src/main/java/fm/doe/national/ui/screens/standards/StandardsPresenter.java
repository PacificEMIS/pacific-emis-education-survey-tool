package fm.doe.national.ui.screens.standards;

import com.arellomobile.mvp.InjectViewState;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class StandardsPresenter extends BasePresenter<StandardsView> {

    private final long passingId;
    private final long categoryId;
    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();

    public StandardsPresenter(long passingId, long categoryId) {
        this.passingId = passingId;
        this.categoryId = categoryId;
        loadPassing();
        loadStandards();
    }

    private void loadPassing() {
        addDisposable(dataSource.requestSchoolAccreditationPassing(passingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(passing -> {
                    StandardsView view = getViewState();
                    view.setSurveyYear(passing.getYear());
                    view.setSchoolName(passing.getSchool().getName());

                    CategoryProgress progress = passing.getSchoolAccreditation().getCategoryProgress();
                    view.setGlobalProgress(progress.getAnsweredQuestionsCount(), progress.getTotalQuestionsCount());
                }, this::handleError));
    }

    private void loadStandards() {
        addDisposable(dataSource.requestStandards(passingId, categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(getViewState()::showStandards, this::handleError));
    }

    public void onStandardClicked(Standard standard) {
        getViewState().navigateToCriteriasScreen(passingId, standard.getId());
    }
}
