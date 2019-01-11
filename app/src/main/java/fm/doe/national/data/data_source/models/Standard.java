package fm.doe.national.data.data_source.models;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Standard extends Identifiable {
    @NonNull
    String getName();

    CategoryProgress getCategoryProgress();

    @Nullable
    String getIndex();

}
