package fm.doe.national.core.data.serialization;

import java.io.InputStream;

import fm.doe.national.core.data.exceptions.ParseException;

public interface Parser<T> {

    T parse(InputStream dataStream) throws ParseException;

}
