package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Collection;

public interface Criteria extends Serializable {
    Standard getStandard();

    @NonNull
    String getName();

    Collection<? extends SubCriteria> getSubCriterias();
}
