package org.pacific_emis.surveys.core.data.serialization;

import java.io.InputStream;

import org.pacific_emis.surveys.core.data.exceptions.ParseException;

public interface Parser<T> {

    T parse(InputStream dataStream) throws ParseException;

}
