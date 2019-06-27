package fm.doe.national.cloud.ui.cloud.dropbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.cloud.di.CloudComponent;
import fm.doe.national.core.data.exceptions.AuthenticationException;
import fm.doe.national.core.data.exceptions.PickerDeclinedException;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.core.utils.Constants;
import fm.doe.national.cloud.model.dropbox.BrowsingTreeObject;
import fm.doe.national.cloud.model.dropbox.DropboxCloudAccessor;

@InjectViewState
public class DropboxPresenter extends BasePresenter<DropboxView> {

    private DropboxView.Action currentAction;
    private final DropboxCloudAccessor cloudAccessor;

    public DropboxPresenter(CloudComponent component,
                            DropboxView.Action action,
                            @Nullable DropboxView.PickerType pickerType,
                            @Nullable BrowsingTreeObject browsingRoot) {
        cloudAccessor = component.getDropboxCloudAccessor();
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