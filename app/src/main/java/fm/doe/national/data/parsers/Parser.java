package fm.doe.national.data.parsers;

import java.io.InputStream;

import fm.doe.national.data.data_source.models.SchoolAccreditation;

public interface Parser<T> {

    T parse(InputStream dataStream);

}
