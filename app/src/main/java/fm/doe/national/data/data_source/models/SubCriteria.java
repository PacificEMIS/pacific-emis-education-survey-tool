package fm.doe.national.data.data_source.models;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// override equals and hashCode in implementations
public interface SubCriteria extends Identifiable {

    @NonNull
    String getName();

    Answer getAnswer();

    SubCriteriaQuestion getSubCriteriaQuestion();

    @Nullable
    String getIndex();

}
