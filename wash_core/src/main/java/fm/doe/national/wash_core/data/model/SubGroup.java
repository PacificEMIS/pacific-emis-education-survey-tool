package fm.doe.national.wash_core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import fm.doe.national.core.data.model.IdentifiedObject;
import fm.doe.national.core.data.model.Progressable;

public interface SubGroup extends Progressable, IdentifiedObject {

    @NonNull
    String getTitle();

    @NonNull
    String getPrefix();

    @Nullable
    List<? extends Question> getQuestions();

}
