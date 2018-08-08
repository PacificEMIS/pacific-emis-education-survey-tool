package fm.doe.national.data.converters;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.sql.SQLException;

import fm.doe.national.data.data_source.db.dao.CriteriaDao;
import fm.doe.national.data.data_source.db.dao.DatabaseHelper;
import fm.doe.national.data.data_source.db.dao.GroupStandardDao;
import fm.doe.national.data.data_source.db.dao.StandardDao;
import fm.doe.national.data.data_source.db.dao.SubCriteriaDao;
import fm.doe.national.data.data_source.db.models.OrmLiteCriteria;
import fm.doe.national.data.data_source.db.models.OrmLiteGroupStandard;
import fm.doe.national.data.data_source.db.models.OrmLiteStandard;
import fm.doe.national.data.data_source.db.models.OrmLiteSubCriteria;
import fm.doe.national.models.survey.Criteria;
import fm.doe.national.models.survey.Standard;

public class SchoolImporter implements DataImporter {

    private Gson gson;
    private DatabaseHelper databaseHelper;

    public SchoolImporter(Gson gson, DatabaseHelper databaseHelper) {
        this.gson = gson;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void importData(String data) {

    }
}
