package fm.doe.national.data.model.mutable;

import androidx.annotation.NonNull;

import java.util.List;

import fm.doe.national.app_support.utils.CollectionUtils;
import fm.doe.national.data.model.Category;
import fm.doe.national.data.model.EvaluationForm;
import fm.doe.national.data.persistence.entity.relative.RelativeRoomCategory;

public class MutableCategory extends BaseMutableEntity implements Category {

    private String title;
    private List<MutableStandard> standards;
    private MutableProgress progress = MutableProgress.createEmptyProgress();
    private EvaluationForm evaluationForm;

    public MutableCategory(@NonNull Category other) {
        this.id = other.getId();
        this.title = other.getTitle();
        this.standards = CollectionUtils.map(other.getStandards(), MutableStandard::new);
        this.evaluationForm = other.getEvaluationForm();
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

    public MutableProgress getProgress() {
        return progress;
    }

    public void setProgress(MutableProgress progress) {
        this.progress = progress;
    }

    @Override
    public EvaluationForm getEvaluationForm() {
        return evaluationForm;
    }

    public void setEvaluationForm(EvaluationForm evaluationForm) {
        this.evaluationForm = evaluationForm;
    }
}
