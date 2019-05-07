package fm.doe.national.data.serialization.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import fm.doe.national.data.model.School;
import fm.doe.national.data.persistence.entity.PersistenceSchool;

public class CsvSchoolParser implements Parser<List<School>> {

    private interface Column {
        int SCHOOL_NUMBER = 0;
        int NAME = 1;
    }

    @Override
    public List<School> parse(InputStream dataStream) throws ParseException {
        List<School> schoolList = new ArrayList<>();

        try {
            CsvReader reader = new CsvReader();
            reader.setContainsHeader(true);

            CsvParser parser = reader.parse(new InputStreamReader(dataStream));
            CsvRow row;

            while ((row = parser.nextRow()) != null) {
                String schoolNumber = row.getField(Column.SCHOOL_NUMBER);
                String name = row.getField(Column.NAME);

                if (schoolNumber != null && name != null) {
                    schoolList.add(new PersistenceSchool(name, schoolNumber));
                }
            }
        } catch (IOException e) {
            throw new ParseException();
        }
        if (schoolList.isEmpty()) throw new ParseException();
        return schoolList;
    }
}
