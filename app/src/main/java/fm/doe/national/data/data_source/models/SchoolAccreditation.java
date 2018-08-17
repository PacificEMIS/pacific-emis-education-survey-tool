package fm.doe.national.data.data_source.models;

import java.util.Collection;

public interface SchoolAccreditation extends Survey {
    Collection<? extends GroupStandard> getGroupStandards();
}
