package fm.doe.national.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import fm.doe.national.data.model.Answer;
import fm.doe.national.data.model.SubCriteria;
import fm.doe.national.data.persistence.entity.relative.RelativePersistenceSubCriteria;

public class MutableSubCriteria extends BaseMutableEntity implements SubCriteria {

    private String suffix;
    private String title;
    private String interviewQuestions;
    private String hint;
    private MutableAnswer answer;

    public MutableSubCriteria() {
    }

    public MutableSubCriteria(@NonNull SubCriteria otherSubCriteria) {
        this.id = otherSubCriteria.getId();
        this.suffix = otherSubCriteria.getSuffix();
        this.title = otherSubCriteria.getTitle();
        this.interviewQuestions = otherSubCriteria.getInterviewQuestions();
        this.hint = otherSubCriteria.getHint();
        Answer answer = otherSubCriteria.getAnswer();
        if (answer != null) {
            this.answer = new MutableAnswer(answer);
        }
    }

    public MutableSubCriteria(@NonNull RelativePersistenceSubCriteria other) {
        this(other.subCriteria);
        if (other.answers != null && !other.answers.isEmpty()) {
            this.answer = new MutableAnswer(other.answers.get(0));
        }
    }

    @NonNull
    @Override
    public String getSuffix() {
        return suffix;
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getInterviewQuestions() {
        return interviewQuestions;
    }

    @Nullable
    @Override
    public String getHint() {
        return hint;
    }

    @NonNull
    @Override
    public MutableAnswer getAnswer() {
        return answer;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInterviewQuestions(String interviewQuestions) {
        this.interviewQuestions = interviewQuestions;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setAnswer(MutableAnswer answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutableSubCriteria criteria = (MutableSubCriteria) o;
        return getId() == criteria.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
