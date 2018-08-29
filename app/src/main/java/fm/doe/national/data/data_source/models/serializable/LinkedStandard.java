package fm.doe.national.data.data_source.models.serializable;

import java.util.List;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.Standard;

public interface LinkedStandard extends Standard {
    List<? extends Criteria> getCriterias();
}
