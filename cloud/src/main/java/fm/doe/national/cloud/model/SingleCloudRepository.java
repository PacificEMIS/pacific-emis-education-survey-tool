package fm.doe.national.cloud.model;

import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class SingleCloudRepository implements CloudRepository {

    private final CloudAccessor accessor;

    public SingleCloudRepository(CloudAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public Completable auth(CloudType type) {
        return accessor.auth();
    }

    @Override
    public Completable uploadContent(String content, String filename) {
        return accessor.exportContentToCloud(content, filename);
    }

    @Override
    public Single<String> requestContent(CloudType type) {
        return accessor.importContentFromCloud();
    }

    @Override
    public Completable chooseExportFolder(CloudType type) {
        return accessor.selectExportFolder();
    }

    @Override
    public void setPrimary(CloudType type) {
        // nothing
    }

    @Override
    public List<CloudAccountData> getUsedAccounts() {
        return Collections.singletonList(new CloudAccountData(
                accessor.getType(),
                accessor.getEmail(),
                accessor.getExportPath(),
                true));
    }

    @Override
    public List<CloudAccountData> getUnusedAccounts() {
        return Collections.emptyList();
    }
}
