package fm.doe.national.ui.screens.base;

import com.dropbox.core.NetworkIOException;
import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.R;
import fm.doe.national.data.cloud.exceptions.AuthenticationException;
import fm.doe.national.data.cloud.exceptions.FileExportException;
import fm.doe.national.data.cloud.exceptions.FileImportException;
import fm.doe.national.data.cloud.exceptions.PickException;
import fm.doe.national.data.parsers.Parser;
import io.reactivex.exceptions.OnErrorNotImplementedException;

public class BasePresenter<T extends BaseView> extends BaseDisposablePresenter<T> {
    protected void handleError(Throwable throwable) {
        if (throwable instanceof NetworkIOException) {
            getViewState().showWarning(Text.from(R.string.title_error), Text.from(R.string.error_network));
        } else if (throwable instanceof AuthenticationException) {
            getViewState().showWarning(Text.from(R.string.title_error), Text.from(R.string.error_auth));
        } else if (throwable instanceof FileExportException) {
            getViewState().showWarning(Text.from(R.string.title_error), Text.from(R.string.error_export));
        } else if (throwable instanceof FileImportException) {
            getViewState().showWarning(Text.from(R.string.title_error), Text.from(R.string.error_import));
        } else if (throwable instanceof PickException) {
            getViewState().showWarning(Text.from(R.string.title_error), Text.from(R.string.error_picker));
        } else if (throwable instanceof Parser.ParseException) {
            getViewState().showWarning(Text.from(R.string.title_error), Text.from(R.string.error_parse));
        } else if (throwable instanceof OnErrorNotImplementedException) {
            getViewState().showWarning(Text.from(R.string.title_error), Text.from(R.string.error_any));
        } else if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        } else {
            getViewState().showWarning(Text.from(R.string.title_error), Text.from(R.string.error_any));
        }
    }
}
