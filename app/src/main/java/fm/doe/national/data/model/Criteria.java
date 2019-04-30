package fm.doe.national.data.model;

import androidx.annotation.NonNull;

import java.util.List;

public interface Criteria extends IdentifiedObject {

    @NonNull
    String getTitle();

    @NonNull
    String getSuffix();

    @NonNull
    List<SubCriteria> getSubCriterias();
}
