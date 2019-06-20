package fm.doe.national.offline_sync.data.devices;

public interface InitiatorDevice {

    void sendConnect(String deviceId);

    void sendSurveyRequest(Long surveyId);

}
