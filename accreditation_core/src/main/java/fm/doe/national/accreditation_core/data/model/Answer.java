package fm.doe.national.accreditation_core.data.model;

import fm.doe.national.core.data.model.BaseAnswer;
import fm.doe.national.core.data.model.IdentifiedObject;

public interface Answer extends IdentifiedObject, BaseAnswer {

    AnswerState getState();

}
