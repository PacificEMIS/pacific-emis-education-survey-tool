package org.pacific_emis.surveys.core.data.serialization.converters;

import org.pacific_emis.surveys.core.preferences.entities.UploadState;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

public class UploadStateConverter implements Converter<UploadState> {

    @Override
    public UploadState read(InputNode node) throws Exception {
        final String value = node.getValue();
        return UploadState.getOrDefault(value);
    }

    @Override
    public void write(OutputNode node, UploadState value) throws Exception {
        node.setValue(value.name());
    }
}
