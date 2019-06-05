package fm.doe.national.wash_core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import fm.doe.national.core.data.model.IdentifiedObject;
import fm.doe.national.core.data.model.Progressable;
import fm.doe.national.wash_core.data.serialization.model.Variant;

public interface Question extends Progressable, IdentifiedObject {

    @NonNull
    String getTitle();

    @NonNull
    String getPrefix();

    @NonNull
    QuestionType getType();

    @Nullable
    List<String> getItems();

    @Nullable
    List<Variant> getVariants();
}
