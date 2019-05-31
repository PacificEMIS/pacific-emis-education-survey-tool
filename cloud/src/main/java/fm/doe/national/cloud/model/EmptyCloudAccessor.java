package fm.doe.national.cloud.model;


import androidx.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Single;

public class EmptyCloudAccessor implements CloudAccessor {

    @Override
    public Single<String> importContentFromCloud() {
        return Single.fromCallable(() -> "");
    }

    @Override
    public Completable exportContentToCloud(@NonNull String content, @NonNull String filename) {
        return Completable.complete();
    }

    @Override
    public Completable auth() {
        return Completable.complete();
    }

    @Override
    public Completable selectExportFolder() {
        return Completable.complete();
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getExportPath() {
        return "";
    }

    @Override
    public boolean isInUse() {
        return false;
    }

    @Override
    public CloudType getType() {
        return CloudType.EMPTY;
    }
}
