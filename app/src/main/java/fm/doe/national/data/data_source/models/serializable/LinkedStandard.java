package fm.doe.national.data.data_source.models.serializable;


import java.util.List;

import androidx.annotation.Nullable;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.Standard;

public interface LinkedStandard extends Standard {
    @Nullable
    List<? extends Criteria> getCriterias();
}
