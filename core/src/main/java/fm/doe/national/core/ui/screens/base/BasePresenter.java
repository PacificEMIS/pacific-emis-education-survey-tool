package fm.doe.national.core.ui.screens.base;

import androidx.annotation.NonNull;

import com.dropbox.core.NetworkIOException;
import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.R;
import fm.doe.national.core.data.exceptions.AuthenticationException;
import fm.doe.national.core.data.exceptions.FileExportException;
import fm.doe.national.core.data.exceptions.FileImportException;
import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.data.exceptions.ParseException;
import fm.doe.national.core.data.exceptions.PickException;
import fm.doe.national.core.data.exceptions.PickerDeclinedException;
import io.reactivex.exceptions.OnErrorNotImplementedException;

public class BasePresenter<T extends BaseView> extends BaseDisposablePresenter<T> {
    protected void handleError(Throwable throwable) {
        Text errorTitle = Text.from(R.string.title_error);
        if (throwable instanceof NotImplementedException) {
            getViewState().showMessage(errorTitle, Text.from(R.string.coming_soon));
        } else if (throwable instanceof NetworkIOException) {
            getViewState().showMessage(errorTitle, Text.from(R.string.error_network));
        } else if (throwable instanceof AuthenticationException) {
            getViewState().showMessage(errorTitle, Text.from(R.string.error_auth));
        } else if (throwable instanceof FileExportException) {
            getViewState().showMessage(errorTitle, Text.from(R.string.error_export));
        } else if (throwable instanceof FileImportException) {
            getViewState().showMessage(errorTitle, Text.from(R.string.error_import));
        } else if (throwable instanceof PickerDeclinedException) {
            // nothing - user just declined picker
        } else if (throwable instanceof PickException) {
            getViewState().showMessage(errorTitle, Text.from(R.string.error_picker));
        } else if (throwable instanceof ParseException) {
            getViewState().showMessage(errorTitle, Text.from(R.string.error_parse));
        } else if (throwable instanceof OnErrorNotImplementedException) {
            getViewState().showMessage(errorTitle, Text.from(R.string.error_any));
        } else if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        } else {
            getViewState().showMessage(errorTitle, Text.from(R.string.error_any));
        }
    }

    @NonNull
    protected String provideMasterPassword() {
        return "";
    }

    @NonNull
    protected String provideFactoryPassword() {
        return "";
    }

    protected void onMasterPasswordValidated() {
        // nothing
    }

    public void onMasterPasswordSubmit(String submittedPassword) {
        if (submittedPassword.equals(provideFactoryPassword()) || submittedPassword.equals(provideMasterPassword())) {
            onMasterPasswordValidated();
        } else {
            getViewState().showMessage(Text.from(R.string.title_error), Text.from(R.string.message_invalid_password));
        }
    }
}
