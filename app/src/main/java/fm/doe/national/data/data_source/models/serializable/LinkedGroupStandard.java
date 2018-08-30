package fm.doe.national.data.data_source.models.serializable;

import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;

public interface LinkedGroupStandard extends GroupStandard {
    List<? extends LinkedStandard> getStandards();
}
