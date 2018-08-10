package fm.doe.national.data.models.survey;

import android.support.annotation.NonNull;

import java.util.Collection;

public interface GroupStandard {
    @NonNull
    String getName();

    Collection<? extends Standard> getStandards();
}
