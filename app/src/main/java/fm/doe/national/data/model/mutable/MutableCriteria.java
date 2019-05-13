package fm.doe.national.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

import fm.doe.national.data.model.Criteria;
import fm.doe.national.data.persistence.entity.relative.RelativePersistenceCriteria;
import fm.doe.national.domain.model.Progress;
import fm.doe.national.utils.CollectionUtils;

public class MutableCriteria extends BaseMutableEntity implements Criteria {

    private String title;
    private String suffix;
    private List<MutableSubCriteria> subCriteriaList;
    private Progress progress = new Progress(0, 0);

    public MutableCriteria() {
    }

    public MutableCriteria(@NonNull Criteria other) {
        this.id = other.getId();
        this.title = other.getTitle();
        this.suffix = other.getSuffix();
        this.subCriteriaList = CollectionUtils.map(other.getSubCriterias(), MutableSubCriteria::new);
    }

    public MutableCriteria(@NonNull RelativePersistenceCriteria other) {
        this(other.criteria);
        this.subCriteriaList = CollectionUtils.map(other.subCriterias, MutableSubCriteria::new);
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
    public List<MutableSubCriteria> getSubCriterias() {
        return subCriteriaList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setSubCriterias(@Nullable List<MutableSubCriteria> subCriterias) {
        this.subCriteriaList = subCriterias;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutableCriteria criteria = (MutableCriteria) o;
        return getId() == criteria.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
