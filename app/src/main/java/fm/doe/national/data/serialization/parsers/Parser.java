package fm.doe.national.data.serialization.parsers;

import java.io.InputStream;

import fm.doe.national.core.data.exceptions.ParseException;

public interface Parser<T> {

    T parse(InputStream dataStream) throws ParseException;

}
