package org.pacific_emis.surveys.wash_core.data.serialization.converters;

import android.text.TextUtils;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.util.Arrays;

import org.pacific_emis.surveys.wash_core.data.model.QuestionType;

public class QuestionTypeConverter implements Converter<QuestionType> {

    @Override
    public QuestionType read(InputNode node) throws Exception {
        try {
            return QuestionType.createFromFlags(Arrays.stream(node.getValue().split("\\|"))
                    .map(flag -> QuestionType.Flag.valueOf(flag).getValue())
                    .reduce((lv, rv) -> lv + rv)
                    .orElse(0));
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void write(OutputNode node, QuestionType value) {
        node.setValue(TextUtils.join("|", value.getFlags()));
    }

}
