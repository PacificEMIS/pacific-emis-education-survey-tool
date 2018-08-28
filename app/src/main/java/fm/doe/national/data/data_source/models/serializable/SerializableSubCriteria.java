package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.SubCriteria;

@Xml(name = "subcriteria")
public class SerializableSubCriteria implements SubCriteria {

    @PropertyElement
    String name;

    @Element
    SerializableAnswer answer;

    public SerializableSubCriteria() {
    }

    public SerializableSubCriteria(String name, SerializableAnswer answer) {
        this.name = name;
        this.answer = answer;
    }

    @Nullable
    @Override
    public Criteria getCriteria() {
        return null;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAnswerCount() {
        return getAnswer() != null && getAnswer().getState() != Answer.State.NOT_ANSWERED ? 1 : 0;
    }

    @Override
    public void setAnswerCount(int count) {
        // nothing
    }

    @Override
    public Answer getAnswer() {
        return answer;
    }

    @Override
    public void setAnswer(Answer answer) {
        this.answer = new SerializableAnswer(answer);
    }

    @Override
    public Long getId() {
        return null;
    }
}
