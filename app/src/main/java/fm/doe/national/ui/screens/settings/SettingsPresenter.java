package fm.doe.national.ui.screens.settings;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import fm.doe.national.R;
import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.files.PicturesRepository;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.parsers.Parser;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.data.cloud.CloudAccountData;
import fm.doe.national.data.cloud.CloudType;
import fm.doe.national.domain.SettingsInteractor;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> {

    private final SettingsInteractor interactor = MicronesiaApplication.getInjection().getAppComponent().getSettingsInteractor();
    private final GlobalPreferences globalPreferences = MicronesiaApplication.getInjection().getCoreComponent().getGlobalPreferences();
    private final PicturesRepository picturesRepository = MicronesiaApplication.getInjection().getCoreComponent().getPicturesRepository();
    private final DataSource dataSource = MicronesiaApplication.getInjection().getCoreComponent().getDataSource();
    private final Parser<Survey> surveyParser = MicronesiaApplication.getInjection().getCoreComponent().getSurveyParser();
    private final Parser<List<School>> schoolsParser = MicronesiaApplication.getInjection().getCoreComponent().getSchoolsParser();

    public SettingsPresenter() {
        loadLogo();
        updateUi();
        getViewState().setAppContext(globalPreferences.getAppRegion());
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
                .subscribe(() -> {
                    interactor.setDefaultCloudForExport(type);
                    updateUi();
                }, this::handleError));
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
        addDisposable(Observable.fromIterable(connectedAccounts)
                .map(CloudAccountData::getType)
                .toList()
                .subscribe(getViewState()::hideConnectViews));
    }

    public void onChangeLogoClick() {
        getViewState().pickPhotoFromGallery();
    }

    public void onImagePicked(Bitmap bitmap) {
        try {
            File pictureFile = picturesRepository.createEmptyFile();
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            bitmap.recycle();
            String filePath = pictureFile.getPath();
            globalPreferences.setLogoPath(filePath);
            loadLogo();
        } catch (IOException ex) {
            ex.printStackTrace();
            getViewState().showMessage(Text.from(R.string.title_error), Text.from(R.string.error_save_logo));
        }
    }

    private void loadLogo() {
        String logoPath = globalPreferences.getLogoPath();
        if (logoPath != null) {
            getViewState().setLogo(logoPath);
            getViewState().setLogoName(new File(logoPath).getName());
        }
    }

    // TODO: this is temporary method, will be implemented in total redesign feature
    public void onAppContextEntered(String value, AssetManager assetManager) {
        try {
            AppRegion appRegion = AppRegion.createFromValue(Integer.parseInt(value));
            if (appRegion != null) {
                globalPreferences.setAppRegion(appRegion);
                addDisposable(
                        Single.fromCallable(() -> surveyParser.parse(assetManager.open(dataSource.getSurveyTemplateFileName())))
                                .flatMapCompletable(dataSource::rewriteTemplateSurvey)
                                .andThen(Single.fromCallable(() -> schoolsParser.parse(assetManager.open(dataSource.getSchoolsFileName()))))
                                .flatMapCompletable(dataSource::rewriteAllSchools)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe(d -> getViewState().showWaiting())
                                .subscribe(() -> {
                                    getViewState().hideWaiting();
                                    getViewState().showToast(Text.from(R.string.toast_set_app_context_success));
                                }, this::handleError));
            }
        } catch (NumberFormatException nfe) {
            // do nothing
        }
    }
}
