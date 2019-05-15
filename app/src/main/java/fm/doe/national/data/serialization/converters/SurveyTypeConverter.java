package fm.doe.national.data.serialization.converters;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import fm.doe.national.data.model.SurveyType;

public class SurveyTypeConverter implements Converter<SurveyType> {

    @Override
    public SurveyType read(InputNode node) throws Exception {
        final String value = node.getValue();
        return SurveyType.valueOf(value);
    }


    @Override
    public void write(OutputNode node, SurveyType value) {
        node.setValue(value.name());
    }

}