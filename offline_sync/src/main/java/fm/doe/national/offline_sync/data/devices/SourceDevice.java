package fm.doe.national.offline_sync.data.devices;

import java.util.List;

import fm.doe.national.core.data.model.Survey;

public interface SourceDevice {

    void sendUnfilledSurveyList(List<Survey> surveyList);

    void sendFilledSurvey(Survey survey);

}
