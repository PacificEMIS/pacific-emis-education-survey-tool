package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.SubCriteriaAddition;

@Root(name = "subcriteria")
public class SerializableSubCriteria implements SubCriteria {

    @Element
    String name;

    @Element(required = false)
    SerializableAnswer answer;

    @Nullable
    @Element(required = false)
    String interviewQuestions;

    @Nullable
    @Element(required = false)
    String hint;

    private TemporarySubCriteriaAddition addition;

    public SerializableSubCriteria() {
    }

    public SerializableSubCriteria(String name, SerializableAnswer answer, SubCriteriaAddition addition) {
        this.name = name;
        this.answer = answer;
        this.addition = new TemporarySubCriteriaAddition(addition.getInterviewQuestions(), addition.getHint());
        this.interviewQuestions = addition.getInterviewQuestions();
        this.hint = addition.getInterviewQuestions();
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Answer getAnswer() {
        return answer;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public SubCriteriaAddition getSubCriteriaAddition() {
        return addition != null ? addition : new TemporarySubCriteriaAddition(interviewQuestions, hint);
    }

    private class TemporarySubCriteriaAddition implements SubCriteriaAddition {

        @Nullable
        private String interviewQuestions;

        @Nullable
        private String hint;

        public TemporarySubCriteriaAddition(@Nullable String interviewQuestions, @Nullable String hint) {
            this.interviewQuestions = interviewQuestions;
            this.hint = hint;
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
    }

}
