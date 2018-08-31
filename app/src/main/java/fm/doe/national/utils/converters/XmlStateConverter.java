package fm.doe.national.utils.converters;

import com.tickaroo.tikxml.TypeConverter;

import fm.doe.national.data.data_source.models.Answer;

public class XmlStateConverter implements TypeConverter<Answer.State> {
    @Override
    public Answer.State read(String value) {
        return Answer.State.valueOf(value);
    }

    @Override
    public String write(Answer.State value) {
        return value.name();
    }
}
