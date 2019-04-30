package fm.doe.national.data.serialization.parsers;

import java.io.InputStream;

public interface Parser<T> {

    T parse(InputStream dataStream) throws ParseException;

    class ParseException extends Exception {
        // nothing
    }
}
