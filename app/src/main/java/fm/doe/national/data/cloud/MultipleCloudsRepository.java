package fm.doe.national.data.cloud;

import java.util.Map;

import fm.doe.national.data.cloud.exceptions.AuthenticationException;
import fm.doe.national.data.cloud.exceptions.FileExportException;
import fm.doe.national.data.cloud.exceptions.FileImportException;
import fm.doe.national.data.cloud.exceptions.PickException;
import io.reactivex.Completable;
import io.reactivex.Single;

public class MultipleCloudsRepository implements CloudRepository {

    private Map<CloudAccessor.Type, CloudAccessor> accessorMap;
    private CloudAccessor.Type primaryType = CloudAccessor.Type.DRIVE;

    public MultipleCloudsRepository(Map<CloudAccessor.Type, CloudAccessor> accessorMap) {
        this.accessorMap = accessorMap;
    }

    @Override
    public Completable auth() {
        for (Map.Entry<CloudAccessor.Type, CloudAccessor> entry : accessorMap.entrySet()) {
            if (entry.getKey() == primaryType) {
                return entry.getValue().auth();
            }
        }
        return Completable.fromAction(() -> {
            throw new AuthenticationException("Cloud accessors not found");
        });
    }

    @Override
    public Completable uploadContent(String content, String filename) {
        for (Map.Entry<CloudAccessor.Type, CloudAccessor> entry : accessorMap.entrySet()) {
            if (entry.getKey() == primaryType) {
                return entry.getValue().exportContentToCloud(content, filename);
            }
        }
        return Completable.fromAction(() -> {
            throw new FileExportException("Cloud accessors not found");
        });
    }

    @Override
    public Single<String> requestContent() {
        for (Map.Entry<CloudAccessor.Type, CloudAccessor> entry : accessorMap.entrySet()) {
            if (entry.getKey() == primaryType) {
                return entry.getValue().importContentFromCloud();
            }
        }
        return Single.fromCallable(() -> {
            throw new FileImportException("Cloud accessors not found");
        });
    }

    @Override
    public Completable chooseExportFolder() {
        for (Map.Entry<CloudAccessor.Type, CloudAccessor> entry : accessorMap.entrySet()) {
            if (entry.getKey() == primaryType) return entry.getValue().selectExportFolder();
        }
        return Completable.fromAction(() -> {
            throw new PickException("Cloud accessors not found");
        });
    }

    @Override
    public void setPrimary(CloudAccessor.Type type) {
        this.primaryType = type;
    }
}
