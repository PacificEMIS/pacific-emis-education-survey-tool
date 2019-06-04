package fm.doe.national.core.data.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import fm.doe.national.core.data.exceptions.ParseException;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.model.mutable.MutableSchool;

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
                    schoolList.add(new MutableSchool(schoolNumber, name));
                }
            }
        } catch (IOException e) {
            throw new ParseException();
        }
        if (schoolList.isEmpty()) throw new ParseException();
        return schoolList;
    }
}
