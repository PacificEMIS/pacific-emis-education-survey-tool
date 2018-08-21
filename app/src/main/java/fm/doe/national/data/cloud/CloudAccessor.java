package fm.doe.national.data.cloud;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface CloudAccessor {
    Type getType();
    Single<String> importContentFromCloud();
    Completable exportContentToCloud(@NonNull String content);
    Completable auth();

    enum Type {
        DRIVE, DROPBOX
    }
}
