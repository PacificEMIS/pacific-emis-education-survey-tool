package fm.doe.national.data.persistence.new_model;

import androidx.annotation.NonNull;

import java.util.List;

public interface Standard extends IdentifiedObject {

    @NonNull
    String getTitle();

    @NonNull
    String getSuffix();

    @NonNull
    List<Criteria> getCriterias();
}
