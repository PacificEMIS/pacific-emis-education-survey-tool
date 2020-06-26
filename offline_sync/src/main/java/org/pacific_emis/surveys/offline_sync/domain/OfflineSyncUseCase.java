package org.pacific_emis.surveys.offline_sync.domain;

import org.pacific_emis.surveys.core.data.model.Survey;
import io.reactivex.Completable;

public interface OfflineSyncUseCase {

    void executeAsInitiator(Survey survey);

    Completable executeAsReceiver();

    void finish();

    Survey getTargetSurvey();

    void setExternalSurvey(Survey survey);

    Survey getExternalSurvey();
}
