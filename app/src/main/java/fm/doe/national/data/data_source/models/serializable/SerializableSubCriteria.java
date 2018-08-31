package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import fm.doe.national.data.data_source.models.Answer;
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
