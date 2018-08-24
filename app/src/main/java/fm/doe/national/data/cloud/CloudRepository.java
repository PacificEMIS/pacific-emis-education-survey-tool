package fm.doe.national.data.cloud;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface CloudRepository {
    Completable authenticate();
    Completable uploadContent(String content);
    Single<String> getContent();
    Completable chooseExportFolder();
    void setPrimary(CloudAccessor.Type type);
}
