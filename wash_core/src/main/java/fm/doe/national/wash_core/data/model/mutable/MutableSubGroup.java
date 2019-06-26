package fm.doe.national.wash_core.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.model.mutable.BaseMutableEntity;
import fm.doe.national.core.data.model.mutable.MutableProgress;
import fm.doe.national.core.utils.CollectionUtils;
import fm.doe.national.wash_core.data.model.Question;
import fm.doe.national.wash_core.data.model.SubGroup;

public class MutableSubGroup extends BaseMutableEntity implements SubGroup {

    @NonNull
    private String title;

    @NonNull
    private String prefix;

    @Nullable
    List<MutableQuestion> questions;

    @NonNull
    private MutableProgress progress;

    public MutableSubGroup(SubGroup other) {
        this(other.getTitle(), other.getPrefix());

        this.id = other.getId();

        if (other.getQuestions() != null) {
            this.questions = other.getQuestions().stream().map(MutableQuestion::new).collect(Collectors.toList());
        }
    }

    public MutableSubGroup(@NonNull String title, @NonNull String prefix) {
        this.title = title;
        this.prefix = prefix;
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

    @Nullable
    @Override
    public List<MutableQuestion> getQuestions() {
        return questions;
    }

    @NonNull
    @Override
    public MutableProgress getProgress() {
        return progress;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setPrefix(@NonNull String prefix) {
        this.prefix = prefix;
    }

    public void setQuestions(@Nullable List<MutableQuestion> questions) {
        this.questions = questions;
    }

    public void setProgress(@NonNull MutableProgress progress) {
        this.progress = progress;
    }

    public List<MutableAnswer> merge(SubGroup other) {
        List<? extends Question> externalQuestions = other.getQuestions();
        List<MutableAnswer> changedAnswers = new ArrayList<>();

        if (!CollectionUtils.isEmpty(externalQuestions)) {
            for (Question question : externalQuestions) {
                for (MutableQuestion mutableQuestion : getQuestions()) {
                    if (mutableQuestion.getPrefix().equals(question.getPrefix())) {
                        MutableAnswer changedAnswer = mutableQuestion.merge(question);

                        if (changedAnswer != null) {
                            changedAnswers.add(changedAnswer);
                        }

                        break;
                    }
                }
            }
        }

        return changedAnswers;
    }
}
