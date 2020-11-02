package org.pacific_emis.surveys.core.data.serialization;

import org.pacific_emis.surveys.core.data.model.Survey;

public interface SurveySerializer {
    String serialize(Survey data);
}
