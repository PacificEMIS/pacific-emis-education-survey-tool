package org.pacific_emis.surveys.accreditation_core.data.serialization;

import org.pacific_emis.surveys.core.utils.DateUtils;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UtcDateConverter implements Converter<Date> {
    // TODO: apply UTC
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z", Locale.US);

    @Override
    public Date read(InputNode node) throws Exception {
        final String value = node.getValue();
        // TODO: apply UTC
//        return value == null ? null : DateUtils.parseUtc(value);
        return value == null ? null : dateFormat.parse(value);
    }


    @Override
    public void write(OutputNode node, Date value) {
        // TODO: apply UTC
//        node.setValue(value == null ? null : DateUtils.formatUtc(value));
        node.setValue(value == null ? null : dateFormat.format(value));
    }

}
