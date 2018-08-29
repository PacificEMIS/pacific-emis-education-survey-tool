package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;

import java.util.List;

public interface Standard extends Identifiable {
    @NonNull
    String getName();

    CategoryProgress getCategoryProgress();

}
