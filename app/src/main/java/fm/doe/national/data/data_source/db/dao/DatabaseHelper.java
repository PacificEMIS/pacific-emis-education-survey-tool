package fm.doe.national.data.data_source.db.dao;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import fm.doe.national.BuildConfig;
import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.models.db.OrmLiteAnswer;
import fm.doe.national.data.data_source.models.db.OrmLiteSchool;
import fm.doe.national.data.data_source.models.db.OrmLiteSurvey;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyPassing;
import fm.doe.national.data.data_source.models.serializable.SerializableSchoolAccreditation;
import fm.doe.national.utils.StreamUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "NDOE_Data_Collection.db";

    private SchoolDao schoolDao;
    private SurveyDao surveyDao;
    private SurveyItemDao surveyItemDao;
    private AnswerDao answerDao;
    private SurveyPassingDao surveyPassingDao;

    private AssetManager assetManager;
    private Gson gson;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, BuildConfig.DATA_BASE_VERSION);
        assetManager = context.getAssets();
        gson = MicronesiaApplication.getAppComponent().getGson();
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        createAllTables(connectionSource);
//        createSurvey();
    }

    // Remove after cloud storage integration
    private void createSurvey() {
        try {
            InputStream inputStream = assetManager.open(BuildConfig.SURVEYS_FILE_NAME);
            String data = StreamUtils.asString(inputStream);

            SerializableSchoolAccreditation schoolAccreditation =
                    gson.fromJson(data, SerializableSchoolAccreditation.class);

            surveyDao.createSchoolAccreditation(
                    schoolAccreditation.getVersion(),
                    schoolAccreditation.getType(),
                    schoolAccreditation.getGroupStandards());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createAllTables(ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, OrmLiteSchool.class);
            TableUtils.createTable(connectionSource, OrmLiteSurvey.class);
            TableUtils.createTable(connectionSource, OrmLiteSurveyItem.class);
            TableUtils.createTable(connectionSource, OrmLiteSurveyPassing.class);
            TableUtils.createTable(connectionSource, OrmLiteAnswer.class);
        } catch (SQLException exc) {
            Log.e(TAG, "Error create Db " + DATABASE_NAME);
            throw new RuntimeException(exc);
        }
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase database,
            ConnectionSource connectionSource,
            int oldVersion,
            int newVersion) {
        // Now we don't need this method
        dropAllTables();
        createAllTables(connectionSource);
        createSurvey();
    }

    private void dropAllTables() {
        try {
            TableUtils.dropTable(connectionSource, OrmLiteSchool.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteSurvey.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteSurveyItem.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteSurveyPassing.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteAnswer.class, true);
        } catch (SQLException exc) {
            Log.e(TAG, "Error drop Db " + DATABASE_NAME);
            throw new RuntimeException(exc);
        }
    }

    @NonNull
    public SchoolDao getSchoolDao() throws SQLException {
        if (schoolDao == null) {
            schoolDao = new SchoolDao(getConnectionSource(), OrmLiteSchool.class);
        }
        return schoolDao;
    }

    public SurveyDao getSurveyDao() throws SQLException {
        if (surveyDao == null) {
            surveyDao = new SurveyDao(getSurveyItemDao(), getConnectionSource(), OrmLiteSurvey.class);
        }
        return surveyDao;
    }

    public SurveyItemDao getSurveyItemDao() throws SQLException {
        if (surveyItemDao == null) {
            surveyItemDao = new SurveyItemDao(getConnectionSource(), OrmLiteSurveyItem.class);
        }
        return surveyItemDao;
    }

    public AnswerDao getAnswerDao() throws SQLException {
        if (answerDao == null) {
            answerDao = new AnswerDao(getConnectionSource(), OrmLiteAnswer.class);
        }
        return answerDao;
    }

    public SurveyPassingDao getSurveyPassingDao() throws SQLException {
        if (surveyPassingDao == null) {
            surveyPassingDao = new SurveyPassingDao(
                    getSurveyDao(),
                    getConnectionSource(),
                    OrmLiteSurveyPassing.class);
        }
        return surveyPassingDao;
    }
}
