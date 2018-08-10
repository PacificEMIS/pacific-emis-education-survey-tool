package fm.doe.national.data.models.survey;

import android.support.annotation.NonNull;

import java.util.List;

public interface Standard {
    GroupStandard getGroupStandard();

    @NonNull
    String getName();

    List<? extends Criteria> getCriterias();
}
