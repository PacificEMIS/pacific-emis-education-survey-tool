package fm.doe.national.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import fm.doe.national.data.model.Answer;
import fm.doe.national.data.model.SubCriteria;

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

    @NonNull
    @Override
    public String getSuffix() {
        return index == null ? "" : index;
    }

    @NonNull
    @Override
    public String getTitle() {
        return name;
    }

    @Nullable
    @Override
    public String getInterviewQuestions() {
        return interviewQuestion;
    }

    @Nullable
    @Override
    public String getHint() {
        return hint;
    }

    @Nullable
    @Override
    public Answer getAnswer() {
        return answer;
    }

    @Override
    public long getId() {
        return 0;
    }
}
