package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface Standard extends Identifiable {
    @NonNull
    String getName();

    CategoryProgress getCategoryProgress();

    @Nullable
    Integer getIndex();

}
