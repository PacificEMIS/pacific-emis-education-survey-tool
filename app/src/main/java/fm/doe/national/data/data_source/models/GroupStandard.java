package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;

public interface GroupStandard extends Identifiable {
    @NonNull
    String getName();

    CategoryProgress getCategoryProgress();
}
