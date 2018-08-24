package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;

import java.util.List;

public interface Criteria extends Identifiable {
    Standard getStandard();

    @NonNull
    String getName();

    List<? extends SubCriteria> getSubCriterias();
}
