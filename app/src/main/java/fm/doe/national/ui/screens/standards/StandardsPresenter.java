package fm.doe.national.ui.screens.standards;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.data.model.Standard;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class StandardsPresenter extends BasePresenter<StandardsView> {

    private final long categoryId;
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
                .map(mutableStandards -> new ArrayList<Standard>(mutableStandards))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(standards -> {
                    StandardsView view = getViewState();
                    view.showStandards(standards);

                    int completedCount = 0;
                    for (Standard standard : standards) {
                        Progress progress = standard.getProgress();
                        if (progress.getCompleted() == progress.getTotal()) completedCount++;
                    }
                    view.setGlobalProgress(completedCount, standards.size());
                }, this::handleError));
    }

    public void onStandardClicked(Standard standard) {
        getViewState().navigateToCriteriasScreen(categoryId, standard.getId());
    }
}
