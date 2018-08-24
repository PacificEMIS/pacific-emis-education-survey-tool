package fm.doe.national.data.data_source.models;

import java.util.List;

public interface SchoolAccreditation extends Survey {
    List<? extends GroupStandard> getGroupStandards();
}
