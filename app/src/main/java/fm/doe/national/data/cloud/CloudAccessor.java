package fm.doe.national.data.cloud;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface CloudAccessor {
    Single<String> importContentFromCloud();

    Completable exportContentToCloud(@NonNull String content, @NonNull String filename);

    Completable auth();

    Completable selectExportFolder();

    enum Type {
        DRIVE, DROPBOX
    }
}
