package fm.doe.national.data.cloud;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface CloudRepository {
    Completable auth();

    Completable uploadContent(String content, String filename);

    Single<String> requestContent();

    Completable chooseExportFolder();

    void setPrimary(CloudAccessor.Type type);
}