package fm.doe.national.ui.screens.authentication;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.domain.SettingsInteractor;

@InjectViewState
public class AuthenticationPresenter extends BasePresenter<AuthenticationView> {

    private final SettingsInteractor interactor = MicronesiaApplication.getInjection().getAppComponent().getSettingsInteractor();

    public AuthenticationPresenter() {
//        addDisposable(interactor.auth(CloudType.DRIVE)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(disposable -> getViewState().showWaiting())
//                .doFinally(() -> getViewState().hideWaiting())
//                .subscribe(() -> {
//                    interactor.setDefaultCloudForExport(CloudType.DRIVE);
                    getViewState().navigateToRegionChoose();
//                }, this::handleError));
    }
}
