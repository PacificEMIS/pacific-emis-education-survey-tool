package org.pacific_emis.surveys.wash_core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import org.pacific_emis.surveys.core.data.model.Answerable;
import org.pacific_emis.surveys.core.data.model.IdentifiedObject;
import org.pacific_emis.surveys.wash_core.data.serialization.model.Relation;

public interface Question extends IdentifiedObject, Answerable {

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
