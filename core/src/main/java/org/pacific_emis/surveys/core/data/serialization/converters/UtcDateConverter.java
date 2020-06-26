package org.pacific_emis.surveys.core.data.serialization.converters;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.util.Date;

import org.pacific_emis.surveys.core.utils.DateUtils;


public class UtcDateConverter implements Converter<Date> {

    @Override
    public Date read(InputNode node) throws Exception {
        final String value = node.getValue();
        return DateUtils.parseUtc(value);
    }

    @Override
    public void write(OutputNode node, Date value) {
        node.setValue(DateUtils.formatUtc(value));
    }

}