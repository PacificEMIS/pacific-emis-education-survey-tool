package fm.doe.national.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import fm.doe.national.data.model.Category;
import fm.doe.national.data.persistence.entity.relative.RelativePersistenceCategory;
import fm.doe.national.utils.CollectionUtils;

public class MutableCategory extends BaseMutableEntity implements Category {

    private String title;
    private List<MutableStandard> standards;

    public MutableCategory(@NonNull Category other) {
        this.id = other.getId();
        this.title = other.getTitle();
        this.standards = CollectionUtils.map(other.getStandards(), MutableStandard::new);
    }

    public MutableCategory() {
    }

    public MutableCategory(@NonNull RelativePersistenceCategory other) {
        this(other.category);
        this.standards = CollectionUtils.map(other.standards, MutableStandard::new);
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public List<MutableStandard> getStandards() {
        return standards;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStandards(List<MutableStandard> standards) {
        this.standards = standards;
    }
}
