package fm.doe.national.data.serialization.converters;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import fm.doe.national.data.model.EvaluationForm;

public class EvaluationFormConverter implements Converter<EvaluationForm> {

    @Override
    public EvaluationForm read(InputNode node) throws Exception {
        final String value = node.getValue();
        return EvaluationForm.valueOf(value);
    }


    @Override
    public void write(OutputNode node, EvaluationForm value) {
        node.setValue(value.name());
    }

}
