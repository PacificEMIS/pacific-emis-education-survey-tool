package fm.doe.national.data.cloud;

import java.util.Map;

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
        return Completable.complete();
    }

    @Override
    public Completable uploadContent(String content, String filename) {
        for (Map.Entry<CloudAccessor.Type, CloudAccessor> entry : accessorMap.entrySet()) {
            if (entry.getKey() == primaryType) {
                return entry.getValue().exportContentToCloud(content, filename);
            }
        }
        return Completable.complete();
    }

    @Override
    public Single<String> getContent() {
        for (Map.Entry<CloudAccessor.Type, CloudAccessor> entry : accessorMap.entrySet()) {
            if (entry.getKey() == primaryType) {
                return entry.getValue().importContentFromCloud();
            }
        }
        return Single.just("");
    }

    @Override
    public Completable chooseExportFolder() {
        for (Map.Entry<CloudAccessor.Type, CloudAccessor> entry : accessorMap.entrySet()) {
            if (entry.getKey() == primaryType) return entry.getValue().selectExportFolder();
        }
        return Completable.complete();
    }

    @Override
    public void setPrimary(CloudAccessor.Type type) {
        this.primaryType = type;
    }
}
