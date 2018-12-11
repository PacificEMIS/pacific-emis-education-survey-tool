package fm.doe.national.data.data_source.models.serializable;

import java.util.List;

import fm.doe.national.data.data_source.models.Category;

public interface LinkedCategory extends Category {
    List<? extends LinkedStandard> getStandards();
}
