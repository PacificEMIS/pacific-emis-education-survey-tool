package fm.doe.national.data.data_source.models.serializable;

import java.util.List;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.SubCriteria;

public interface LinkedSchoolAccreditation extends SchoolAccreditation {
    List<? extends LinkedGroupStandard> getGroupStandards();
}
