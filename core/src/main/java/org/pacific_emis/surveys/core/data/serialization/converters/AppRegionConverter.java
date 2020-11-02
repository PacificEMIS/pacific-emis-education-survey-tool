package org.pacific_emis.surveys.core.data.serialization.converters;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

public class AppRegionConverter implements Converter<AppRegion> {

    @Override
    public AppRegion read(InputNode node) throws Exception {
        final String value = node.getValue();
        return AppRegion.valueOf(value);
    }


    @Override
    public void write(OutputNode node, AppRegion value) {
        node.setValue(value.name());
    }

}
