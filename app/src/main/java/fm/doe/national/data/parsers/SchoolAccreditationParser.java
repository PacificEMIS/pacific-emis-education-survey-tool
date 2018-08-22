package fm.doe.national.data.parsers;

import java.io.InputStream;

import fm.doe.national.data.data_source.models.SchoolAccreditation;

public interface SchoolAccreditationParser {

    SchoolAccreditation parse(InputStream dataStream);

}
