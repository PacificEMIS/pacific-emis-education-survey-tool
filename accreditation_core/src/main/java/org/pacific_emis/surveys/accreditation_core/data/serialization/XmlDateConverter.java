package org.pacific_emis.surveys.accreditation_core.data.serialization;

import org.pacific_emis.surveys.core.utils.DateUtils;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class XmlDateConverter implements Converter<Date> {
    private static final DateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z", Locale.US);

    @Override
    public Date read(InputNode node) throws Exception {
        final String value = node.getValue();
        if (value == null) {
            return null;
        }
        final Date extractedDate = DateUtils.parseUtc(value);
        if (extractedDate != null) {
            return extractedDate;
        }
        // backward compatibility
        return oldDateFormat.parse(value);
    }


    @Override
    public void write(OutputNode node, Date value) {
        node.setValue(value == null ? null : DateUtils.formatUtc(value));
    }

}
