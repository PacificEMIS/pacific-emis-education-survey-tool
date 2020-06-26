package org.pacific_emis.surveys.accreditation_core.data.model;

import org.pacific_emis.surveys.core.data.model.BaseAnswer;
import org.pacific_emis.surveys.core.data.model.IdentifiedObject;

public interface Answer extends IdentifiedObject, BaseAnswer {

    AnswerState getState();

}
