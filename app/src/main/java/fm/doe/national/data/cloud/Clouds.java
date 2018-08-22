package fm.doe.national.data.cloud;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;

public class Clouds implements CloudRepository {

    private Map<CloudAccessor.Type, CloudAccessor> accessorMap;
    private CloudAccessor.Type primaryType = CloudAccessor.Type.DRIVE;

    public Clouds(Map<CloudAccessor.Type, CloudAccessor> accessorMap) {
        this.accessorMap = accessorMap;
    }

    @Override
    public Completable authenticate() {
        for (Map.Entry<CloudAccessor.Type, CloudAccessor> entry : accessorMap.entrySet()) {
            if (entry.getKey() == primaryType) {
                return entry.getValue().auth();
            }
        }
        return Completable.complete();
    }

    @Override
    public Completable uploadContent(String content) {
        for (Map.Entry<CloudAccessor.Type, CloudAccessor> entry : accessorMap.entrySet()) {
            if (entry.getKey() == primaryType) {
                return entry.getValue().exportContentToCloud(content);
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
    public void setPrimary(CloudAccessor.Type type) {
        this.primaryType = type;
    }
}
