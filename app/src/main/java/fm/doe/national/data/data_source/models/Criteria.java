package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public interface Criteria extends Identifiable {

    @NonNull
    String getName();

    CategoryProgress getCategoryProgress();

    List<? extends SubCriteria> getSubCriterias();

    @Nullable
    String getIndex();
}
