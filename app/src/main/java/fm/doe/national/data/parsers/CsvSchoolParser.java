package fm.doe.national.data.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.db.OrmLiteSchool;

public class CsvSchoolParser implements Parser<List<School>> {

    private interface Column {
        String SCHOOL_NUMBER = "schoolNumber";
        String NAME = "name";
    }

    @Override
    public List<School> parse(InputStream dataStream) {
        List<School> schoolList = new ArrayList<>();

        try {
            CsvReader reader = new CsvReader();
            reader.setContainsHeader(true);

            CsvParser parser = reader.parse(new InputStreamReader(dataStream));
            CsvRow row;

            while ((row = parser.nextRow()) != null) {
                String schoolNumber = row.getField(Column.SCHOOL_NUMBER);
                String name = row.getField(Column.NAME);
                schoolList.add(new OrmLiteSchool(schoolNumber, name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return schoolList;
    }
}
