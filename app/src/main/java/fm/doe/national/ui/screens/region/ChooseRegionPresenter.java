package fm.doe.national.ui.screens.region;

import com.omegar.mvp.InjectViewState;

import java.util.Arrays;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.domain.SettingsInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ChooseRegionPresenter extends BasePresenter<ChooseRegionView> {

    private final SettingsInteractor interactor = MicronesiaApplication.getInjection().getAppComponent().getSettingsInteractor();

    private AppRegion selectedRegion = AppRegion.values()[0];

    public ChooseRegionPresenter() {
        getViewState().setRegions(Arrays.asList(AppRegion.values()));
    }

    public void onRegionSelected(AppRegion region) {
        selectedRegion = region;
    }

    public void onContinuePressed() {
        interactor.setAppRegion(selectedRegion);
        addDisposable(interactor.loadDataFromAssets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().showWaiting())
                .subscribe(() -> {
                    getViewState().hideWaiting();
                    getViewState().navigateToMenu();
                }, this::handleError));
    }
}
