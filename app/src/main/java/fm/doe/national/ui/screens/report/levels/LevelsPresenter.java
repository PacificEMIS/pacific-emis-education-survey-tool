package fm.doe.national.ui.screens.report.levels;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.domain.ReportInteractor;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class LevelsPresenter extends BasePresenter<LevelsView> {

    private final ReportInteractor interactor = MicronesiaApplication.getAppComponent().getReportInteractor();

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
