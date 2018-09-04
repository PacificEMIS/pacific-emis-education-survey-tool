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

    public SerializableSubCriteria(String name, SerializableAnswer answer) {
        this.name = name;
        this.answer = answer;
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
}
