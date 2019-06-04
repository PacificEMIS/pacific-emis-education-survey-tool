package fm.doe.national.accreditation_core.data.model;

import androidx.annotation.Nullable;

import java.util.List;

import fm.doe.national.core.data.model.IdentifiedObject;
import fm.doe.national.core.data.model.Progressable;
import fm.doe.national.core.data.model.Survey;

public interface AccreditationSurvey extends Progressable, IdentifiedObject, Survey {

    @Nullable
    List<? extends Category> getCategories();

}
