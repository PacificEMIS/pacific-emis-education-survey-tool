package fm.doe.national.ui.screens.cloud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.data.cloud.dropbox.BrowsingTreeObject;
import fm.doe.national.data.cloud.dropbox.DropboxCloudAccessor;
import fm.doe.national.data.cloud.exceptions.AuthenticationException;
import fm.doe.national.data.cloud.exceptions.PickerDeclinedException;
import fm.doe.national.ui.screens.base.BasePresenter;
import fm.doe.national.app_support.utils.Constants;

@InjectViewState
public class DropboxPresenter extends BasePresenter<DropboxView> {

    private DropboxView.Action currentAction;
    private final DropboxCloudAccessor cloudAccessor = MicronesiaApplication.getAppComponent().getDropboxCloudAccessor();

    public DropboxPresenter(DropboxView.Action action, @Nullable DropboxView.PickerType pickerType, @Nullable BrowsingTreeObject browsingRoot) {
        currentAction = action;
        switch (currentAction) {
            case AUTH:
                getViewState().startAuthentication();
                break;
            case PICK:
                if (browsingRoot == null) {
                    endingCloudAccessorAction(() -> cloudAccessor.onActionFailure(
                            new IllegalStateException(Constants.Errors.WRONG_INTENT)));
                } else {
                    getViewState().showPicker(pickerType, browsingRoot);
                }
                break;

        }
    }

    public void checkAuthResult() {
        if (currentAction == DropboxView.Action.AUTH) {
            endingCloudAccessorAction(() -> {
                if (cloudAccessor.isSuccessfulAuth()) {
                    cloudAccessor.onAuthActionComplete();
                } else {
                    cloudAccessor.onActionFailure(new AuthenticationException(Constants.Errors.AUTH_DECLINED));
                }
            });
        }
    }

    public void onBrowsingItemPicked(BrowsingTreeObject object) {
        endingCloudAccessorAction(() -> cloudAccessor.onPickerSuccess(object));
    }

    public void pickerCancelled() {
        endingCloudAccessorAction(() -> cloudAccessor.onActionFailure(new PickerDeclinedException()));
    }

    private void endingCloudAccessorAction(@NonNull Runnable action) {
        action.run();
        getViewState().exit();
    }
}
