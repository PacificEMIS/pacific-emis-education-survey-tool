package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;

public interface SubCriteria extends Identifiable {

    @NonNull
    String getName();

    Answer getAnswer();

    SubCriteriaQuestion getSubCriteriaQuestion();

}
