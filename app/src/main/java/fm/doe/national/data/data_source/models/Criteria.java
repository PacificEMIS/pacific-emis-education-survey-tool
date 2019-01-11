package fm.doe.national.data.data_source.models;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Criteria extends Identifiable {

    @NonNull
    String getName();

    CategoryProgress getCategoryProgress();

    List<? extends SubCriteria> getSubCriterias();

    @Nullable
    String getIndex();
}
