package fm.doe.national.data.data_source.models.survey;

import android.support.annotation.NonNull;

import java.util.Collection;

public interface Standard {
    GroupStandard getGroupStandard();

    @NonNull
    String getName();

    Collection<? extends Criteria> getCriterias();
}
