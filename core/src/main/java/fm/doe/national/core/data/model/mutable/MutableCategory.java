package fm.doe.national.core.data.model.mutable;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.model.Category;
import fm.doe.national.core.data.model.EvaluationForm;

public class MutableCategory extends BaseMutableEntity implements Category {

    private String title;
    private List<MutableStandard> standards;
    private MutableProgress progress = MutableProgress.createEmptyProgress();
    private EvaluationForm evaluationForm;

    public MutableCategory(@NonNull Category other) {
        this.id = other.getId();
        this.title = other.getTitle();
        this.evaluationForm = other.getEvaluationForm();
        if (other.getStandards() != null) {
            this.standards = other.getStandards().stream().map(MutableStandard::new).collect(Collectors.toList());
        }
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

    @NonNull
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
