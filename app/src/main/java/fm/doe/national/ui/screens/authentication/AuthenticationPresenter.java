package fm.doe.national.ui.screens.authentication;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.domain.SettingsInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class AuthenticationPresenter extends BasePresenter<AuthenticationView> {

    private final SettingsInteractor interactor = MicronesiaApplication.getInjection().getAppComponent().getSettingsInteractor();

    public AuthenticationPresenter() {
        addDisposable(interactor.signIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(() -> getViewState().navigateToRegionChoose(), this::handleError));
    }
}
