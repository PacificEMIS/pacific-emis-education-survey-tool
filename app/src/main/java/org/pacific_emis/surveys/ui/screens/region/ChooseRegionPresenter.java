package org.pacific_emis.surveys.ui.screens.region;

import com.omegar.mvp.InjectViewState;

import java.util.Arrays;

import org.pacific_emis.surveys.app_support.MicronesiaApplication;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.domain.SettingsInteractor;
import org.pacific_emis.surveys.remote_settings.model.RemoteSettings;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ChooseRegionPresenter extends BasePresenter<ChooseRegionView> {

    private final SettingsInteractor interactor = MicronesiaApplication.getInjection()
            .getAppComponent()
            .getSettingsInteractor();
    private final RemoteSettings remoteSettings = MicronesiaApplication.getInjection()
            .getRemoteSettingsComponent()
            .getRemoteSettings();

    private AppRegion selectedRegion = AppRegion.values()[0];

    public ChooseRegionPresenter() {
        getViewState().setRegions(Arrays.asList(AppRegion.values()));
    }

    public void onRegionSelected(AppRegion region) {
        selectedRegion = region;
    }

    public void onContinuePressed() {
        interactor.setAppRegion(selectedRegion);
        addDisposable(
                remoteSettings.forceFetch()
                        .flatMapCompletable(b -> interactor.loadDataFromAssets())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .subscribe(() -> {
                            remoteSettings.init(null);
                            getViewState().hideWaiting();
                            getViewState().navigateToMenu();
                        }, this::handleError));
    }
}
