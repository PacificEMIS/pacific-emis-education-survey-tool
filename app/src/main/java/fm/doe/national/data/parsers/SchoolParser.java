package fm.doe.national.data.parsers;

import java.io.InputStream;
import java.util.List;

import fm.doe.national.data.data_source.models.School;

public interface SchoolParser {

    List<School> parse(InputStream dataStream);

}
