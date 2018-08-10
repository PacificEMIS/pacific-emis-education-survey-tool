package fm.doe.national.data.models.survey;

import android.support.annotation.NonNull;

public interface SubCriteria {
    Criteria getCriteria();

    @NonNull
    String getName();
}
