package fm.doe.national.ui.screens.report.levels;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.ui.screens.report.base.BaseReportPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class LevelsPresenter extends BaseReportPresenter<LevelsView> {

    LevelsPresenter() {
        loadSchoolAccreditationLevel();
    }

    private void loadSchoolAccreditationLevel() {
        addDisposable(interactor.getLevelSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().setLoadingVisible(true))
                .doFinally(() -> getViewState().setLoadingVisible(false))
                .subscribe(getViewState()::setData, this::handleError));
    }
}
