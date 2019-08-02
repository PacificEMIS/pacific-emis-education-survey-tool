package fm.doe.national.core.data.serialization.converters;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import fm.doe.national.core.data.model.SurveyState;

public class SurveyStateConverter implements Converter<SurveyState> {

    @Override
    public SurveyState read(InputNode node) throws Exception {
        final String value = node.getValue();
        return SurveyState.fromValue(value);
    }


    @Override
    public void write(OutputNode node, SurveyState value) {
        node.setValue(value.getValue());
    }

}
