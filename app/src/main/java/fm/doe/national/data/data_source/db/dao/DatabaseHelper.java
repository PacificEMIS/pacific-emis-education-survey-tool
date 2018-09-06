package fm.doe.national.data.data_source.db.dao;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import fm.doe.national.BuildConfig;
import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.db.OrmLiteAnswer;
import fm.doe.national.data.data_source.models.db.OrmLiteCategoryProgress;
import fm.doe.national.data.data_source.models.db.OrmLiteSchool;
import fm.doe.national.data.data_source.models.db.OrmLiteSubCriteriaQuestion;
import fm.doe.national.data.data_source.models.db.OrmLiteSurvey;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyPassing;
import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import fm.doe.national.data.parsers.Parser;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "NDOE_Data_Collection.db";

    private final Parser<LinkedSchoolAccreditation> schoolAccreditationParser =
            MicronesiaApplication.getAppComponent().getSchoolAccreditationParser();
    private final Parser<List<School>> schoolsParser =
            MicronesiaApplication.getAppComponent().getSchoolsParser();

    private SchoolDao schoolDao;
    private SurveyDao surveyDao;
    private SurveyItemDao surveyItemDao;
    private AnswerDao answerDao;
    private SurveyPassingDao surveyPassingDao;
    private CategoryProgressDao categoryProgressDao;
    private SubcriteriaQuestionDao subcriteriaQuestionDao;

    private AssetManager assetManager;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, BuildConfig.DATA_BASE_VERSION);
        assetManager = context.getAssets();
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        createAllTables(connectionSource);
    }

    // TODO: Remove after cloud storage integration
    private void createSurvey() {
        try {
            InputStream inputStream = assetManager.open(BuildConfig.SURVEYS_FILE_NAME);
            LinkedSchoolAccreditation schoolAccreditation = schoolAccreditationParser.parse(inputStream);

            surveyDao.createSchoolAccreditation(
                    schoolAccreditation.getVersion(),
                    schoolAccreditation.getType(),
                    schoolAccreditation.getGroupStandards())
                    .subscribe();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: Remove after cloud storage integration
    private void createSchools() {
        try {
            InputStream inputStream = assetManager.open(BuildConfig.SCHOOLS_FILE_NAME);
            List<School> schools = schoolsParser.parse(inputStream);

            schoolDao.addSchools(schools).subscribe();

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
            TableUtils.createTable(connectionSource, OrmLiteCategoryProgress.class);
            TableUtils.createTable(connectionSource, OrmLiteSubCriteriaQuestion.class);
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
            TableUtils.dropTable(connectionSource, OrmLiteCategoryProgress.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteSubCriteriaQuestion.class, true);
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
            surveyItemDao = new SurveyItemDao(getSubcriteriaQuestionDao(), getConnectionSource(), OrmLiteSurveyItem.class);
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

    public CategoryProgressDao getCategoryProgressDao() throws SQLException {
        if (categoryProgressDao == null) {
            categoryProgressDao = new CategoryProgressDao(
                    getConnectionSource(),
                    OrmLiteCategoryProgress.class);
        }
        return categoryProgressDao;
    }

    public SubcriteriaQuestionDao getSubcriteriaQuestionDao() throws SQLException {
        if (subcriteriaQuestionDao == null) {
            subcriteriaQuestionDao = new SubcriteriaQuestionDao(
                    getConnectionSource(),
                    OrmLiteSubCriteriaQuestion.class);
        }
        return subcriteriaQuestionDao;
    }
}
