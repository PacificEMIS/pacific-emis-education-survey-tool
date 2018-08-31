package fm.doe.national.ui.screens.settings;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.CloudAccountData;
import fm.doe.national.data.cloud.CloudType;
import fm.doe.national.domain.SettingsInteractor;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> {

    private final SettingsInteractor interactor = MicronesiaApplication.getAppComponent().getSettingsInteractor();

    public SettingsPresenter() {
        updateUi();
    }

    public void onConnectToDropboxClick() {
        authenticate(CloudType.DRIVE);
    }

    public void onConnectToDriveClick() {
        authenticate(CloudType.DRIVE);
    }

    private void authenticate(CloudType type) {
        addDisposable(interactor.auth(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(this::updateUi, this::handleError));
    }

    public void onImportSchoolsClick(CloudAccountData viewData) {
        addDisposable(interactor.importSchools(viewData.getType())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(this::updateUi, this::handleError));
    }

    public void onImportSurveyClick(CloudAccountData viewData) {
        addDisposable(interactor.importSurvey(viewData.getType())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(this::updateUi, this::handleError));
    }

    public void onChooseFolderClick(CloudAccountData viewData) {
        addDisposable(interactor.selectExportFolder(viewData.getType())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(this::updateUi, this::handleError));
    }

    private void updateUi() {
        SettingsView view = getViewState();
        List<CloudAccountData> connectedAccounts = interactor.getConnectedAccounts();
        view.showAccountConnections(connectedAccounts);
        for (CloudAccountData accountData : connectedAccounts) {
            view.hideConnectView(accountData.getType());
        }
    }

    public void onSetDefaultClick(CloudAccountData viewData) {
        interactor.setDefaultCloudForExport(viewData.getType());
    }

    public void onChangeLogoClick() {
        // nothing for current sprint
    }
}
