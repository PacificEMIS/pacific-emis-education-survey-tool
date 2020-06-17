package fm.doe.national.accreditation_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.Criteria;
import fm.doe.national.accreditation_core.data.model.ObservationInfo;
import fm.doe.national.accreditation_core.data.model.Standard;
import fm.doe.national.core.data.model.ConflictResolveStrategy;
import fm.doe.national.core.data.model.mutable.BaseMutableEntity;
import fm.doe.national.core.data.model.mutable.MutableProgress;
import fm.doe.national.core.utils.CollectionUtils;

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

    public List<MutableAnswer> merge(Standard other, ConflictResolveStrategy strategy) {
        List<? extends Criteria> externalCriterias = other.getCriterias();
        List<MutableAnswer> changedAnswers = new ArrayList<>();

        if (!CollectionUtils.isEmpty(externalCriterias)) {
            for (Criteria criteria : externalCriterias) {
                for (MutableCriteria mutableCriteria : getCriterias()) {
                    if (mutableCriteria.getSuffix().equals(criteria.getSuffix())) {
                        changedAnswers.addAll(mutableCriteria.merge(criteria, strategy));
                        break;
                    }
                }
            }
        }

        return changedAnswers;
    }
}
