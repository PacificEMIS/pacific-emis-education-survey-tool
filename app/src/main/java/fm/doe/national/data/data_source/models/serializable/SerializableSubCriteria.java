package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SubCriteria;

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

    public SerializableSubCriteria() {
    }

    public SerializableSubCriteria(String name, SerializableAnswer answer, @Nullable String interviewQuestions, @Nullable String hint) {
        this.name = name;
        this.answer = answer;
        this.interviewQuestions = interviewQuestions;
        this.hint = hint;
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
