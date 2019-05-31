package fm.doe.national.cloud.model;

import java.util.List;

import fm.doe.national.core.data.model.CloudType;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface CloudRepository {
    Completable auth(CloudType type);

    Completable uploadContent(String content, String filename);

    Single<String> requestContent(CloudType type);

    Completable chooseExportFolder(CloudType type);

    void setPrimary(CloudType type);

    List<CloudAccountData> getUsedAccounts();

    List<CloudAccountData> getUnusedAccounts();
}