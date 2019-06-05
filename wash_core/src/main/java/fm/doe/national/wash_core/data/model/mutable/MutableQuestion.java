package fm.doe.national.wash_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import fm.doe.national.core.data.model.mutable.BaseMutableEntity;
import fm.doe.national.core.data.model.mutable.MutableProgress;
import fm.doe.national.wash_core.data.model.Question;
import fm.doe.national.wash_core.data.model.QuestionType;
import fm.doe.national.wash_core.data.serialization.model.Relation;
import fm.doe.national.wash_core.data.serialization.model.Variant;

public class MutableQuestion extends BaseMutableEntity implements Question {

    @NonNull
    private String title;

    @NonNull
    private String prefix;

    @NonNull
    private QuestionType questionType;

    @Nullable
    private List<String> items;

    @Nullable
    private List<Variant> variants;

    @Nullable
    private Relation relation;

    @Nullable
    private MutableAnswer answer;

    @NonNull
    private MutableProgress progress;

    public MutableQuestion(Question other) {
        this(other.getTitle(), other.getPrefix(), other.getType());

        this.items = other.getItems();
        this.variants = other.getVariants();
        this.relation = other.getRelation();

        if (other.getAnswer() != null) {
            this.answer = new MutableAnswer(other.getAnswer());
        }
    }

    public MutableQuestion(@NonNull String title, @NonNull String prefix, @NonNull QuestionType questionType) {
        this.title = title;
        this.prefix = prefix;
        this.questionType = questionType;
        this.progress = MutableProgress.createEmptyProgress();
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public String getPrefix() {
        return prefix;
    }

    @NonNull
    @Override
    public QuestionType getType() {
        return questionType;
    }

    @Nullable
    @Override
    public List<String> getItems() {
        return items;
    }

    @Nullable
    @Override
    public List<Variant> getVariants() {
        return variants;
    }

    @Nullable
    @Override
    public Relation getRelation() {
        return relation;
    }

    @Nullable
    @Override
    public MutableAnswer getAnswer() {
        return answer;
    }

    @NonNull
    @Override
    public MutableProgress getProgress() {
        return progress;
    }
}
