package fm.doe.national.data.converters;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.sql.SQLException;

import fm.doe.national.data.data_source.db.dao.DatabaseHelper;
import fm.doe.national.data.data_source.db.models.OrmLiteGroupStandard;

public class JsonImporter implements DataImporter {

    private Gson gson;
    private DatabaseHelper databaseHelper;

    public JsonImporter(Gson gson, DatabaseHelper databaseHelper) {
        this.gson = gson;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void importData(String data) {
        try {
            GroupStandardWrapper groupStandardWrapper = gson.fromJson(data, GroupStandardWrapper.class);
            for (OrmLiteGroupStandard groupStandard : groupStandardWrapper.getGroupStandards()) {
                databaseHelper.getGroupStandardDao().create(groupStandard);
            }
        } catch (SQLException | JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
}
