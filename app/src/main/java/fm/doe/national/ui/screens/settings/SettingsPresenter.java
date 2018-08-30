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
        auth(CloudType.DRIVE);
    }

    public void onConnectToDriveClick() {
        auth(CloudType.DRIVE);
    }

    public void onImportSchoolsClick(CloudAccountData viewData) {
        add(interactor.importSchools(viewData.getType())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(this::updateUi, this::handleError));
    }

    public void onImportSurveyClick(CloudAccountData viewData) {
        add(interactor.importSurvey(viewData.getType())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(this::updateUi, this::handleError));
    }

    public void onChooseFolderClick(CloudAccountData viewData) {
        add(interactor.selectExportFolder(viewData.getType())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(this::updateUi, this::handleError));
    }

    public void onSetDefaultClick(CloudAccountData viewData) {
        interactor.setDefaultCloudForExport(viewData.getType());
    }

    public void onChangeLogoClick() {
        // nothing for current sprint
    }

    private void auth(CloudType type) {
        add(interactor.auth(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(this::updateUi, this::handleError));
    }

    private void updateUi() {
        getViewState().showAccountConnections(interactor.getConnectedAccounts());
        List<CloudAccountData> current = interactor.getConnectedAccounts();
        for (CloudAccountData accountData : current) {
            switch (accountData.getType()) {
                case DROPBOX:
                    getViewState().hideDropboxConnect();
                    break;
                case DRIVE:
                    getViewState().hideDriveConnect();
                    break;
            }
        }
    }
}
