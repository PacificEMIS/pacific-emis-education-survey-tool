package fm.doe.national.remote_storage.ui.default_storage;

import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.omegar.mvp.InjectViewState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.remote_storage.data.accessor.RemoteStorageAccessor;
import fm.doe.national.remote_storage.di.RemoteStorageComponent;

@InjectViewState
public class DefaultStoragePresenter extends BasePresenter<DefaultStorageView> {

    private final RemoteStorageAccessor remoteStorageAccessor;

    public DefaultStoragePresenter(RemoteStorageComponent component) {
        remoteStorageAccessor = component.getRemoteStorageAccessor();
        getViewState().showPicker();
    }

    public void onUriReceived(ContentResolver resolver, Uri uri) {
        String fileContent = readContentFromUri(resolver, uri);
        onContentReceived(fileContent);
    }

    @NonNull
    private String readContentFromUri(ContentResolver contentResolver, Uri uri) {
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);

            if (inputStream == null) {
                return RemoteStorageAccessor.EMPTY_CONTENT;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("\n");
                }

                stringBuilder.append(line);
            }

            inputStream.close();
            reader.close();

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return RemoteStorageAccessor.EMPTY_CONTENT;
        }
    }

    public void onNothingReceived() {
        onContentReceived(RemoteStorageAccessor.EMPTY_CONTENT);
    }

    private void onContentReceived(@NonNull String content) {
        remoteStorageAccessor.onContentReceived(content);
        getViewState().close();
    }
}
