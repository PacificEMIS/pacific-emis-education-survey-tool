package fm.doe.national.data.model.mutable;

import androidx.annotation.NonNull;

import java.util.List;

import fm.doe.national.data.model.Standard;
import fm.doe.national.data.persistence.entity.relative.RelativeRoomStandard;
import fm.doe.national.domain.model.Progress;
import fm.doe.national.utils.CollectionUtils;

public class MutableStandard extends BaseMutableEntity implements Standard {

    private String title;
    private String suffix;
    private List<MutableCriteria> criterias;
    private Progress progress = new Progress(0, 0);

    public MutableStandard() {
    }

    public MutableStandard(@NonNull Standard other) {
        this.id = other.getId();
        this.title = other.getTitle();
        this.suffix = other.getSuffix();
        this.criterias = CollectionUtils.map(other.getCriterias(), MutableCriteria::new);
    }

    public MutableStandard(@NonNull RelativeRoomStandard other) {
        this(other.standard);
        this.criterias = CollectionUtils.map(other.criterias, MutableCriteria::new);
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public String getSuffix() {
        return suffix;
    }

    @NonNull
    @Override
    public List<MutableCriteria> getCriterias() {
        return criterias;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setCriterias(List<MutableCriteria> criterias) {
        this.criterias = criterias;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }
}
