package fm.doe.national.core.data.serialization;

import fm.doe.national.core.data.model.Survey;

public interface SurveySerializer {
    String serialize(Survey data);
}
