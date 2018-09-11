package fm.doe.national.ui.screens.settings;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.arellomobile.mvp.InjectViewState;
import com.omega_r.libs.omegatypes.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.cloud.CloudAccountData;
import fm.doe.national.data.cloud.CloudType;
import fm.doe.national.data.files.PicturesRepository;
import fm.doe.national.domain.SettingsInteractor;
import fm.doe.national.ui.screens.base.BasePresenter;
import fm.doe.national.utils.Constants;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> {

    private final SettingsInteractor interactor = MicronesiaApplication.getAppComponent().getSettingsInteractor();
    private final SharedPreferences sharedPreferences = MicronesiaApplication.getAppComponent().getSharedPreferences();
    private final PicturesRepository picturesRepository = MicronesiaApplication.getAppComponent().getPicturesRepository();

    public SettingsPresenter() {
        loadLogo();
        updateUi();
    }

    public void onConnectToDropboxClick() {
        authenticate(CloudType.DROPBOX);
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
                .subscribe(() -> getViewState().showToast(Text.from(R.string.toast_import_schools_success)), this::handleError));
    }

    public void onImportSurveyClick(CloudAccountData viewData) {
        addDisposable(interactor.importSurvey(viewData.getType())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(() -> getViewState().showToast(Text.from(R.string.toast_import_survey_success)), this::handleError));
    }

    public void onChooseFolderClick(CloudAccountData viewData) {
        addDisposable(interactor.selectExportFolder(viewData.getType())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(this::updateUi, this::handleError));
    }

    public void onSetDefaultClick(CloudAccountData viewData) {
        interactor.setDefaultCloudForExport(viewData.getType());
        updateUi();
    }

    private void updateUi() {
        SettingsView view = getViewState();
        List<CloudAccountData> connectedAccounts = interactor.getConnectedAccounts();
        view.showAccountConnections(connectedAccounts);
        for (CloudAccountData accountData : connectedAccounts) {
            view.hideConnectView(accountData.getType());
        }
    }

    public void onChangeLogoClick() {
        getViewState().pickPhotoFromGallery();
    }

    public void onImagePicked(Bitmap bitmap) {
        try {
            File pictureFile = picturesRepository.createEmptyFile();
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            String filePath = pictureFile.getPath();
            sharedPreferences.edit().putString(Constants.PREF_KEY_LOGO_PATH, filePath).apply();
            loadLogo();
        } catch (IOException ex) {
            ex.printStackTrace();
            getViewState().showWarning(Text.from(R.string.title_error), Text.from(R.string.error_save_logo));
        }
    }

    private void loadLogo() {
        String logoPath = sharedPreferences.getString(Constants.PREF_KEY_LOGO_PATH, null);
        if (logoPath != null) {
            getViewState().setLogo(logoPath);
            getViewState().setLogoName(new File(logoPath).getName());
        }
    }
}
