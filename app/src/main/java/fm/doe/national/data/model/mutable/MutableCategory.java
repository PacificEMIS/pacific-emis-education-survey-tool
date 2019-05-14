package fm.doe.national.data.model.mutable;

import androidx.annotation.NonNull;

import java.util.List;

import fm.doe.national.data.model.Category;
import fm.doe.national.data.persistence.entity.relative.RelativeRoomCategory;
import fm.doe.national.domain.model.Progress;
import fm.doe.national.app_support.utils.CollectionUtils;

public class MutableCategory extends BaseMutableEntity implements Category {

    private String title;
    private List<MutableStandard> standards;
    private Progress progress = new Progress(0, 0);

    public MutableCategory(@NonNull Category other) {
        this.id = other.getId();
        this.title = other.getTitle();
        this.standards = CollectionUtils.map(other.getStandards(), MutableStandard::new);
    }

    public MutableCategory() {
    }

    public MutableCategory(@NonNull RelativeRoomCategory other) {
        this(other.category);
        this.standards = CollectionUtils.map(other.standards, MutableStandard::new);
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
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

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }
}
