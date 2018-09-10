package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.SubCriteriaQuestion;
import fm.doe.national.utils.TextUtil;

@Root(name = "subcriteria")
public class SerializableSubCriteria implements SubCriteria {

    @Element
    String name;

    @Element(required = false)
    SerializableAnswer answer;

    @Nullable
    @Element(required = false, name = "interviewQuestions")
    String interviewQuestion;

    @Nullable
    @Element(required = false)
    String hint;

    private SimpleSubCriteriaQuestion question;

    public SerializableSubCriteria() {
    }

    public SerializableSubCriteria(String name, SerializableAnswer answer, SubCriteriaQuestion question) {
        this.name = name;
        this.answer = answer;
        this.question = new SimpleSubCriteriaQuestion(question.getInterviewQuestion(), question.getHint());
        this.interviewQuestion = question.getInterviewQuestion();
        this.hint = question.getInterviewQuestion();
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
    public long getId() {
        return 0;
    }

    @Override
    public SubCriteriaQuestion getSubCriteriaQuestion() {
        return question != null ? question : new SimpleSubCriteriaQuestion(interviewQuestion, hint);
    }

    private class SimpleSubCriteriaQuestion implements SubCriteriaQuestion {

        @Nullable
        private String interviewQuestion;

        @Nullable
        private String hint;

        public SimpleSubCriteriaQuestion(@Nullable String interviewQuestion, @Nullable String hint) {
            this.interviewQuestion = TextUtil.fixLineSeparators(interviewQuestion);
            this.hint = TextUtil.fixLineSeparators(hint);
        }

        @Nullable
        @Override
        public String getInterviewQuestion() {
            return interviewQuestion;
        }

        @Nullable
        @Override
        public String getHint() {
            return hint;
        }
    }

}
