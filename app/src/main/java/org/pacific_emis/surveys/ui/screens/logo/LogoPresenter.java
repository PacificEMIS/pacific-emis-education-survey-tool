package org.pacific_emis.surveys.ui.screens.logo;

import android.graphics.Bitmap;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.app_support.MicronesiaApplication;
import org.pacific_emis.surveys.core.data.files.FilesRepository;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;

@InjectViewState
public class LogoPresenter extends BasePresenter<LogoView> {

    private final LocalSettings localSettings = MicronesiaApplication.getInjection()
            .getCoreComponent()
            .getLocalSettings();

    private final FilesRepository filesRepository = MicronesiaApplication.getInjection()
            .getCoreComponent()
            .getFilesRepository();

    public LogoPresenter() {
        refreshLogo();
    }

    private void refreshLogo() {
        getViewState().setLogo(localSettings.getLogo());
    }

    public void onSetDefaultPressed() {
        localSettings.setLogoPath(null);
        refreshLogo();
    }

    public void onChangeLogoPressed() {
        getViewState().pickImageFromGallery();
    }

    public void onImagePicked(Bitmap bitmap) {
        try {
            File pictureFile = filesRepository.createEmptyImageFile();
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            bitmap.recycle();
            localSettings.setLogoPath(pictureFile.getPath());
            refreshLogo();
        } catch (IOException ex) {
            ex.printStackTrace();
            getViewState().showMessage(Text.from(R.string.title_error), Text.from(R.string.error_save_logo));
        }
    }

}
