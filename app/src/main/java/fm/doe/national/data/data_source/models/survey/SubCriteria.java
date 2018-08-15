package fm.doe.national.data.data_source.models.survey;

import android.support.annotation.NonNull;

public interface SubCriteria {
    Criteria getCriteria();

    @NonNull
    String getName();
}
