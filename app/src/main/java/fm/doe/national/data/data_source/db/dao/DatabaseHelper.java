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
import fm.doe.national.data.converters.GroupStandardWrapper;
import fm.doe.national.data.models.survey.Answer;
import fm.doe.national.data.models.survey.Criteria;
import fm.doe.national.data.models.survey.GroupStandard;
import fm.doe.national.data.models.survey.School;
import fm.doe.national.data.models.survey.Standard;
import fm.doe.national.data.models.survey.SubCriteria;
import fm.doe.national.data.models.survey.Survey;
import fm.doe.national.utils.StreamUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "NDOE_Data_Collection.db";

    private SchoolDao schoolDao;
    private SurveyDao surveyDao;
    private GroupStandardDao groupStandardDao;
    private StandardDao standardDao;
    private CriteriaDao criteriaDao;
    private SubCriteriaDao subCriteriaDao;
    private AnswerDao answerDao;

    private AssetManager assetManager;
    private Gson gson;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, BuildConfig.DATA_BASE_VERSION);
        assetManager = context.getAssets();
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        createAllTables(connectionSource);
        fillAllTables();
    }

    private void fillAllTables() {
        try {
            InputStream inputStream = assetManager.open(BuildConfig.SURVEYS_FILE_NAME);
            String data = StreamUtils.asString(inputStream);

            GroupStandardWrapper groupStandardWrapper = gson.fromJson(data, GroupStandardWrapper.class);
            for (GroupStandard groupStandard : groupStandardWrapper.getGroupStandards()) {
                getGroupStandardDao().create(groupStandard);

                for (Standard standard : groupStandard.getStandards()) {
                    standard.setGroupStandard(groupStandard);
                    getStandardDao().create(standard);

                    for (Criteria criteria : standard.getCriterias()) {
                        criteria.setStandard(standard);
                        getCriteriaDao().create(criteria);

                        for (SubCriteria subCriteria : criteria.getSubCriterias()) {
                            subCriteria.setCriteria(criteria);
                            getSubCriteriaDao().create(subCriteria);
                        }
                    }
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void createAllTables(ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Criteria.class);
            TableUtils.createTable(connectionSource, GroupStandard.class);
            TableUtils.createTable(connectionSource, School.class);
            TableUtils.createTable(connectionSource, Standard.class);
            TableUtils.createTable(connectionSource, SubCriteria.class);
            TableUtils.createTable(connectionSource, Survey.class);
            TableUtils.createTable(connectionSource, Answer.class);
        } catch (SQLException exc) {
            Log.e(TAG, "Error create Db " + DATABASE_NAME);
            throw new RuntimeException(exc);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        // Now we don't need this method
        dropAllTables();
        createAllTables(connectionSource);
        fillAllTables();
    }

    private void dropAllTables() {
        try {
            TableUtils.dropTable(connectionSource, Criteria.class, true);
            TableUtils.dropTable(connectionSource, GroupStandard.class, true);
            TableUtils.dropTable(connectionSource, School.class, true);
            TableUtils.dropTable(connectionSource, Standard.class, true);
            TableUtils.dropTable(connectionSource, SubCriteria.class, true);
            TableUtils.dropTable(connectionSource, Survey.class, true);
            TableUtils.dropTable(connectionSource, Answer.class, true);
        } catch (SQLException exc) {
            Log.e(TAG, "Error drop Db " + DATABASE_NAME);
            throw new RuntimeException(exc);
        }
    }

    @NonNull
    public SchoolDao getSchoolDao() throws SQLException {
        if (schoolDao == null) {
            schoolDao = new SchoolDao(getConnectionSource(), School.class);
        }
        return schoolDao;
    }

    public SurveyDao getSurveyDao() throws SQLException {
        if (surveyDao == null) {
            surveyDao = new SurveyDao(getConnectionSource(), Survey.class);
        }
        return surveyDao;
    }

    public GroupStandardDao getGroupStandardDao() throws SQLException {
        if (groupStandardDao == null) {
            groupStandardDao = new GroupStandardDao(getConnectionSource(), GroupStandard.class);
        }
        return groupStandardDao;
    }

    public StandardDao getStandardDao() throws SQLException {
        if (standardDao == null) {
            standardDao = new StandardDao(getConnectionSource(), Standard.class);
        }
        return standardDao;
    }

    public CriteriaDao getCriteriaDao() throws SQLException {
        if (criteriaDao == null) {
            criteriaDao = new CriteriaDao(getConnectionSource(), Criteria.class);
        }
        return criteriaDao;
    }

    public SubCriteriaDao getSubCriteriaDao() throws SQLException {
        if (subCriteriaDao == null) {
            subCriteriaDao = new SubCriteriaDao(getConnectionSource(), SubCriteria.class);
        }
        return subCriteriaDao;
    }

    public AnswerDao getAnswerDao() throws SQLException {
        if (answerDao == null) {
            answerDao = new AnswerDao(getConnectionSource(), Answer.class);
        }
        return answerDao;
    }
}
