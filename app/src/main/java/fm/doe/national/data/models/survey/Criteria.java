package fm.doe.national.data.models.survey;

import android.support.annotation.NonNull;

import java.util.Collection;

public interface Criteria {
    Standard getStandard();

    @NonNull
    String getName();

    Collection<? extends SubCriteria> getSubCriterias();
}
