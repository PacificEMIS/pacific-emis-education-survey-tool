package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public interface   Criteria extends Serializable {
    Standard getStandard();

    @NonNull
    String getName();

    List<? extends SubCriteria> getSubCriterias();
}
