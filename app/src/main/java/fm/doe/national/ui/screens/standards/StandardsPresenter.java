package fm.doe.national.ui.screens.standards;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.model.Standard;
import fm.doe.national.data.model.mutable.MutableStandard;
import fm.doe.national.domain.SurveyInteractor;
import fm.doe.national.domain.model.Progress;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class StandardsPresenter extends BasePresenter<StandardsView> {

    private final long categoryId;
    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();
    private final SurveyInteractor interactor = MicronesiaApplication.getAppComponent().getSurveyInteractor();

    public StandardsPresenter(long categoryId) {
        this.categoryId = categoryId;
        loadPassing();
    }

    @Override
    public void attachView(StandardsView view) {
        super.attachView(view);
        loadStandards();
    }

    private void loadPassing() {
        addDisposable(Single.fromCallable(interactor::getCurrentSurvey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .doOnSuccess(survey -> getViewState().setSurveyDate(survey.getDate()))
                .flatMap(s -> interactor.requestCategories())
                .flatMapObservable(Observable::fromIterable)
                .filter(category -> category.getId() == categoryId)
                .firstOrError()
                .subscribe(category -> getViewState().setCategoryName(category.getTitle()), this::handleError));
    }

    private void loadStandards() {
        addDisposable(interactor.requestStandards(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(standards -> {
                    StandardsView view = getViewState();
                    view.showStandards(standards);

                    int completedCount = 0;
                    for (MutableStandard standard : standards) {
                        Progress progress = standard.getProgress();
                        if (progress.completed == progress.total) completedCount++;
                    }
                    view.setGlobalProgress(completedCount, standards.size());
                }, this::handleError));
    }

    public void onStandardClicked(Standard standard) {
        getViewState().navigateToCriteriasScreen(categoryId, standard.getId());
    }
}
