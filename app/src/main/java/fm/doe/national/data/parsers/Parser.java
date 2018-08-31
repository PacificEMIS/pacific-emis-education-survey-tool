package fm.doe.national.data.parsers;

import java.io.InputStream;

public interface Parser<T> {

    T parse(InputStream dataStream);

}
