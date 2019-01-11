package fm.doe.national.data.data_source.models;


import java.util.List;

import androidx.annotation.NonNull;

public interface Category extends Identifiable {
    @NonNull
    String getName();

    CategoryProgress getCategoryProgress();

    List<? extends Standard> getStandards();
}
