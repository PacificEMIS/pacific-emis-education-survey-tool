package fm.doe.national.core.ui.screens.base;

import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dropbox.core.NetworkIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.omega_r.libs.omegatypes.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import fm.doe.national.core.R;
import fm.doe.national.core.data.exceptions.AuthenticationException;
import fm.doe.national.core.data.exceptions.FileExportException;
import fm.doe.national.core.data.exceptions.FileImportException;
import fm.doe.national.core.data.exceptions.GmsUserRecoverableException;
import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.data.exceptions.ParseException;
import fm.doe.national.core.data.exceptions.PickException;
import fm.doe.national.core.data.exceptions.PickerDeclinedException;
import fm.doe.national.core.data.exceptions.WrongAppRegionException;
import io.reactivex.exceptions.OnErrorNotImplementedException;

public class BasePresenter<T extends BaseView> extends BaseDisposablePresenter<T> {
    protected void handleError(Throwable throwable) {
        Text errorTitle = Text.from(R.string.title_error);
        if (throwable instanceof NotImplementedException) {
            getViewState().showMessage(errorTitle, Text.from(R.string.coming_soon));
        } else if (throwable instanceof WrongAppRegionException) {
            getViewState().showMessage(errorTitle, Text.from(R.string.error_wrong_region));
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
        } else if (throwable instanceof UserRecoverableAuthIOException) {
            getViewState().handleGmsRecoverableException(
                    new GmsUserRecoverableException(((UserRecoverableAuthIOException) throwable).getIntent()));
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

    public void onExternalDocumentPicked(ContentResolver contentResolver, Uri uri) {
        // do nothing
    }

    @Nullable
    protected String readExternalUriToString(ContentResolver contentResolver, Uri uri) {
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
            String text = scanner.useDelimiter("\\A").next();
            scanner.close();
            return text;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
