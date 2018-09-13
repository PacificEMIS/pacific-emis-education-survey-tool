package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;

import java.util.List;

public interface Category extends Identifiable {
    @NonNull
    String getName();

    CategoryProgress getCategoryProgress();

    List<? extends Standard> getStandards();
}
