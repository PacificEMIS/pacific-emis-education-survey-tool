package fm.doe.national.wash_core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import fm.doe.national.core.data.model.IdentifiedObject;
import fm.doe.national.wash_core.data.serialization.model.Relation;

public interface Question extends IdentifiedObject {

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

    @Nullable
    Relation getRelation();

    @Nullable
    Answer getAnswer();

}
