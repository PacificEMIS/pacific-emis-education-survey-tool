package fm.doe.national.data.converters;

import com.google.gson.Gson;

import fm.doe.national.data.data_source.db.dao.DatabaseHelper;

public class SchoolImporter implements DataImporter {

    private Gson gson;
    private DatabaseHelper databaseHelper;

    public SchoolImporter(Gson gson, DatabaseHelper databaseHelper) {
        this.gson = gson;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void importData(String data) {
        // nothing
    }
}
