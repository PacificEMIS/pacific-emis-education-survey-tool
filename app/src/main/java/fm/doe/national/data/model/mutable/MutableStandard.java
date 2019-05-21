package fm.doe.national.data.model.mutable;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.data.model.Standard;
import fm.doe.national.data.persistence.entity.relative.RelativeRoomStandard;

public class MutableStandard extends BaseMutableEntity implements Standard {

    private String title;
    private String suffix;
    private List<MutableCriteria> criterias;
    private MutableProgress progress = MutableProgress.createEmptyProgress();

    public MutableStandard() {
    }

    public MutableStandard(@NonNull Standard other) {
        this.id = other.getId();
        this.title = other.getTitle();
        this.suffix = other.getSuffix();
        if (other.getCriterias() != null) {
            this.criterias = other.getCriterias().stream().map(MutableCriteria::new).collect(Collectors.toList());
        }
    }

    public MutableStandard(@NonNull RelativeRoomStandard other) {
        this(other.standard);
        this.criterias = other.criterias.stream().map(MutableCriteria::new).collect(Collectors.toList());
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

    @NonNull
    public MutableProgress getProgress() {
        return progress;
    }

    public void setProgress(MutableProgress progress) {
        this.progress = progress;
    }
}
