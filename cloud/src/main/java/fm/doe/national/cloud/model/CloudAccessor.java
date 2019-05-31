package fm.doe.national.cloud.model;

import androidx.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface CloudAccessor {
    Single<String> importContentFromCloud();

    Completable exportContentToCloud(@NonNull String content, @NonNull String filename);

    Completable auth();

    Completable selectExportFolder();

    String getEmail();

    String getExportPath();

    boolean isInUse();

    CloudType getType();
}
