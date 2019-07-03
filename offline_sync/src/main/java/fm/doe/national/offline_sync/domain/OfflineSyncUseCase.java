package fm.doe.national.offline_sync.domain;

import fm.doe.national.core.data.model.Survey;
import io.reactivex.Completable;

public interface OfflineSyncUseCase {

    void executeAsInitiator(Survey survey);

    Completable executeAsReceiver();

    void finish();

    Survey getTargetSurvey();
}
