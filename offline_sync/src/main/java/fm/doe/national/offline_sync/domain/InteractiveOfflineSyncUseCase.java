package fm.doe.national.offline_sync.domain;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.offline_sync.data.model.Device;

public interface InteractiveOfflineSyncUseCase extends OfflineSyncUseCase {

    void onDeviceSelected(Device device);

    void onSurveySelected(Survey survey);

}
