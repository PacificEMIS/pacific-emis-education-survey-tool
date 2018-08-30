package fm.doe.national.data.cloud;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fm.doe.national.data.cloud.exceptions.AuthenticationException;
import fm.doe.national.data.cloud.exceptions.FileExportException;
import fm.doe.national.data.cloud.exceptions.FileImportException;
import fm.doe.national.data.cloud.exceptions.PickException;
import io.reactivex.Completable;
import io.reactivex.Single;

public class MultipleCloudsRepository implements CloudRepository {

    private Map<CloudType, CloudAccessor> accessorMap;
    private CloudType primaryType = CloudType.DRIVE;

    public MultipleCloudsRepository(Map<CloudType, CloudAccessor> accessorMap) {
        this.accessorMap = accessorMap;
    }

    @Override
    public Completable auth(CloudType type) {
        for (Map.Entry<CloudType, CloudAccessor> entry : accessorMap.entrySet()) {
            if (entry.getKey() == type) {
                return entry.getValue().auth();
            }
        }
        return Completable.fromAction(() -> {
            throw new AuthenticationException("Cloud accessors not found");
        });
    }

    @Override
    public Completable uploadContent(String content, String filename) {
        for (Map.Entry<CloudType, CloudAccessor> entry : accessorMap.entrySet()) {
            if (entry.getKey() == primaryType) {
                return entry.getValue().exportContentToCloud(content, filename);
            }
        }
        return Completable.fromAction(() -> {
            throw new FileExportException("Cloud accessors not found");
        });
    }

    @Override
    public Single<String> requestContent(CloudType type) {
        for (Map.Entry<CloudType, CloudAccessor> entry : accessorMap.entrySet()) {
            if (entry.getKey() == type) {
                return entry.getValue().importContentFromCloud();
            }
        }
        return Single.fromCallable(() -> {
            throw new FileImportException("Cloud accessors not found");
        });
    }

    @Override
    public Completable chooseExportFolder(CloudType type) {
        for (Map.Entry<CloudType, CloudAccessor> entry : accessorMap.entrySet()) {
            if (entry.getKey() == type) return entry.getValue().selectExportFolder();
        }
        return Completable.fromAction(() -> {
            throw new PickException("Cloud accessors not found");
        });
    }

    @Override
    public void setPrimary(CloudType type) {
        this.primaryType = type;
    }

    @Override
    public List<CloudAccountData> getUsedAccounts() {
        return getAccountsByUsage(true);
    }

    @Override
    public List<CloudAccountData> getUnusedAccounts() {
        return getAccountsByUsage(false);
    }

    private List<CloudAccountData> getAccountsByUsage(boolean inUse) {
        List<CloudAccountData> accounts = new ArrayList<>();
        for (Map.Entry<CloudType, CloudAccessor> entry : accessorMap.entrySet()) {
            CloudAccessor accessor = entry.getValue();
            if (accessor.inUse() == inUse) {
                accounts.add(new CloudAccountData(
                        entry.getKey(),
                        accessor.getEmail(),
                        accessor.getExportPath(),
                        entry.getKey() == primaryType));
            }
        }
        return accounts;
    }
}
