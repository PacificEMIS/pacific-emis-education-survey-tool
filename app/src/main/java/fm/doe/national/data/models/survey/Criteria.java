package fm.doe.national.data.models.survey;

import android.support.annotation.NonNull;

import java.util.List;

public interface Criteria {
    Standard getStandard();

    @NonNull
    String getName();

    List<? extends SubCriteria> getSubCriterias();
}
