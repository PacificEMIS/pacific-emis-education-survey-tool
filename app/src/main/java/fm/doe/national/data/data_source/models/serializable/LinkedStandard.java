package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.Nullable;

import java.util.List;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.Standard;

public interface LinkedStandard extends Standard {
    @Nullable
    List<? extends Criteria> getCriterias();
}
