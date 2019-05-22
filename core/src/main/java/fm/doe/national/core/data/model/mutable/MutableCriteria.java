package fm.doe.national.core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.model.Criteria;

public class MutableCriteria extends BaseMutableEntity implements Criteria {

    private String title;
    private String suffix;
    private List<MutableSubCriteria> subCriteriaList;
    private MutableProgress progress = MutableProgress.createEmptyProgress();

    public MutableCriteria(@NonNull Criteria other) {
        this.id = other.getId();
        this.title = other.getTitle();
        this.suffix = other.getSuffix();
        if (other.getSubCriterias() != null) {
            this.subCriteriaList = other.getSubCriterias().stream().map(MutableSubCriteria::new).collect(Collectors.toList());
        }
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

    @NonNull
    public MutableProgress getProgress() {
        return progress;
    }

    public void setProgress(MutableProgress progress) {
        this.progress = progress;
    }

}
