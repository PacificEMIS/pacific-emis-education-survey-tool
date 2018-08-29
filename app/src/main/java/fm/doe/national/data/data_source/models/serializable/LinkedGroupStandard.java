package fm.doe.national.data.data_source.models.serializable;

import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.Standard;

public interface LinkedGroupStandard extends GroupStandard {
    List<? extends LinkedStandard> getStandards();
}
