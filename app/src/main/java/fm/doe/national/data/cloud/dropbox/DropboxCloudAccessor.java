package fm.doe.national.data.cloud.dropbox;

import android.content.Context;
import android.support.annotation.NonNull;

import fm.doe.national.data.cloud.CloudAccessor;
import io.reactivex.Completable;
import io.reactivex.Single;

public class DropboxCloudAccessor implements CloudAccessor {

    private Context context;

    public DropboxCloudAccessor(Context appContext) {
        context = appContext;
    }

    @Override
    public Type getType() {
        return Type.DROPBOX;
    }

    @Override
    public Single<String> importContentFromCloud() {
        return null;
    }

    @Override
    public Completable exportContentToCloud(@NonNull String content) {
        return null;
    }

    @Override
    public Completable auth() {
        return null;
    }
}
