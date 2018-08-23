package fm.doe.national.ui.screens.cloud;

import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.dropbox.chooser.android.DbxChooser;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.dropbox.DropboxCloudAccessor;
import fm.doe.national.data.cloud.exceptions.AuthenticationException;
import fm.doe.national.data.cloud.exceptions.FileImportException;
import fm.doe.national.ui.screens.base.BasePresenter;

@InjectViewState
public class DropboxPresenter extends BasePresenter<DropboxView> {

    private DropboxView.Action currentAction;
    private final DropboxCloudAccessor cloudAccessor = MicronesiaApplication.getAppComponent().getDropboxCloudAccessor();

    public DropboxPresenter(DropboxView.Action action) {
        currentAction = action;
        switch (currentAction) {
            case AUTH:
                getViewState().startAuthentication();
                break;
            case PICK_FILE:
                getViewState().showSdkChooser();
                break;
            case UPLOAD_FILE:
                // nothing
                break;
        }
    }

    public void onViewResumedFromPause() {
        switch (currentAction) {
            case AUTH:
                if (cloudAccessor.isSuccessfulAuth()) {
                    cloudAccessor.onAuthActionComplete();
                } else {
                    cloudAccessor.onActionFailure(new AuthenticationException("Auth declined"));
                }
                getViewState().die();
                break;
            case PICK_FILE:
                getViewState().showSdkChooser(); // just rerun
                break;
        }
    }

    public void onCloudFilePathObtained(@Nullable DbxChooser.Result result) {
        if (result != null) {
            cloudAccessor.onCloudFilePathObtained(result);
        } else {
            cloudAccessor.onActionFailure(new FileImportException("File picker declined"));
        }
        getViewState().die();
    }
}
