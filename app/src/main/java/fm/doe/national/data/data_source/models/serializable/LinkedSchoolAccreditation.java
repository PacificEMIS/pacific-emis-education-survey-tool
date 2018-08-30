package fm.doe.national.data.data_source.models.serializable;

import java.util.List;

import fm.doe.national.data.data_source.models.SchoolAccreditation;

public interface LinkedSchoolAccreditation extends SchoolAccreditation {
    List<? extends LinkedGroupStandard> getGroupStandards();
}
