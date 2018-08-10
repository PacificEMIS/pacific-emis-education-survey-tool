package fm.doe.national.data.models.survey;

import android.support.annotation.NonNull;

import java.util.Collection;

public interface Standard {
    GroupStandard getGroupStandard();

    @NonNull
    String getName();

    Collection<? extends Criteria> getCriterias();
}
