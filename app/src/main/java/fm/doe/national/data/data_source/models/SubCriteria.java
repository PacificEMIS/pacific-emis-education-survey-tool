package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


// override equals and hashCode in implementations
public interface SubCriteria extends Identifiable {

    @NonNull
    String getName();

    Answer getAnswer();

    SubCriteriaQuestion getSubCriteriaQuestion();

    @Nullable
    String getIndex();

}
