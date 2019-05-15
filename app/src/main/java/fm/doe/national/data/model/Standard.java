package fm.doe.national.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public interface Standard extends ProgressableObject {

    @NonNull
    String getTitle();

    @NonNull
    String getSuffix();

    @Nullable
    List<? extends Criteria> getCriterias();
}
