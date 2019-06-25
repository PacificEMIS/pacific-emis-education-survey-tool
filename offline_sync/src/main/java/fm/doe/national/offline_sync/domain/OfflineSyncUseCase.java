package fm.doe.national.offline_sync.domain;

import fm.doe.national.core.data.model.Survey;
import io.reactivex.Completable;

public interface OfflineSyncUseCase {
    Completable execute(Survey survey);

    void finish();
}
