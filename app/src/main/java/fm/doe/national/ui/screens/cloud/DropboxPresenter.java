package fm.doe.national.ui.screens.cloud;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.dropbox.BrowsingTreeObject;
import fm.doe.national.data.cloud.dropbox.DropboxCloudAccessor;
import fm.doe.national.data.cloud.exceptions.AuthenticationException;
import fm.doe.national.data.cloud.exceptions.PickException;
import fm.doe.national.ui.screens.base.BasePresenter;

@InjectViewState
public class DropboxPresenter extends BasePresenter<DropboxView> {

    private DropboxView.Action currentAction;
    private final DropboxCloudAccessor cloudAccessor = MicronesiaApplication.getAppComponent().getDropboxCloudAccessor();

    public DropboxPresenter(DropboxView.Action action, @Nullable BrowsingTreeObject browsingRoot) {
        Throwable passingThrowable = new IllegalStateException("browsingRoot not passed to activity");
        currentAction = action;
        switch (currentAction) {
            case AUTH:
                getViewState().startAuthentication();
                break;
            case PICK_FILE:
                if (browsingRoot == null) {
                    endingCloudAccessorAction(() -> cloudAccessor.onActionFailure(passingThrowable));
                } else {
                    getViewState().showFilePicker(browsingRoot);
                }
                break;
            case PICK_FOLDER:
                if (browsingRoot == null) {
                    endingCloudAccessorAction(() -> cloudAccessor.onActionFailure(passingThrowable));
                } else {
                    getViewState().showFolderPicker(browsingRoot);
                }
                break;
        }
    }

    public void onViewResumedFromPause() {
        if (currentAction == DropboxView.Action.AUTH) {
            endingCloudAccessorAction(() -> {
                if (cloudAccessor.isSuccessfulAuth()) {
                    cloudAccessor.onAuthActionComplete();
                } else {
                    cloudAccessor.onActionFailure(new AuthenticationException("Auth declined"));
                }
            });
        }
    }

    public void onBrowsingItemPicked(BrowsingTreeObject object) {
        endingCloudAccessorAction(() -> cloudAccessor.onPickerSuccess(object));
    }

    public void pickerCancelled() {
        endingCloudAccessorAction(() -> cloudAccessor.onActionFailure(new PickException("Picker cancelled")));
    }

    private void endingCloudAccessorAction(@NonNull Runnable action) {
        action.run();
        getViewState().die();
    }
}
