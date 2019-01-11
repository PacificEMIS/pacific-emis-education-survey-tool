package fm.doe.national.data.data_source.models.serializable;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Nullable
    @Element(name = "id", required = false)
    String index;

    private SimpleSubCriteriaQuestion question;

    public SerializableSubCriteria() {
    }

    public SerializableSubCriteria(SubCriteria subCriteria) {
        name = subCriteria.getName();
        answer = new SerializableAnswer(subCriteria.getAnswer());
        index = subCriteria.getIndex();
        SubCriteriaQuestion otherQuestion = subCriteria.getSubCriteriaQuestion();
        if (otherQuestion != null) {
            interviewQuestion = otherQuestion.getInterviewQuestion();
            hint = otherQuestion.getHint();
            question = new SimpleSubCriteriaQuestion(interviewQuestion, hint);
        }
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

    @Nullable
    @Override
    public String getIndex() {
        return index;
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
