package com.micronesia.data_source.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.micronesia.BuildConfig;
import com.micronesia.data_source.db.models.OrmLiteAnswer;
import com.micronesia.data_source.db.models.OrmLiteCriteria;
import com.micronesia.data_source.db.models.OrmLiteGroupStandard;
import com.micronesia.data_source.db.models.OrmLiteSchool;
import com.micronesia.data_source.db.models.OrmLiteStandard;
import com.micronesia.data_source.db.models.OrmLiteSubCriteria;
import com.micronesia.data_source.db.models.OrmLiteSurvey;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "NDOE_Data_Collection.db";

    private SchoolDao schoolDao;
    private SurveyDao surveyDao;
    private GroupStandardDao groupStandardDao;
    private StandardDao standardDao;
    private CriteriaDao criteriaDao;
    private SubCriteriaDao subCriteriaDao;
    private OrmLiteAnswerDao answerDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, BuildConfig.DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        createAllTables(connectionSource);
    }

    private void createAllTables(ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, OrmLiteCriteria.class);
            TableUtils.createTable(connectionSource, OrmLiteGroupStandard.class);
            TableUtils.createTable(connectionSource, OrmLiteSchool.class);
            TableUtils.createTable(connectionSource, OrmLiteStandard.class);
            TableUtils.createTable(connectionSource, OrmLiteSubCriteria.class);
            TableUtils.createTable(connectionSource, OrmLiteSurvey.class);
            TableUtils.createTable(connectionSource, OrmLiteAnswer.class);
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
    }

    private void dropAllTables() {
        try {
            TableUtils.dropTable(connectionSource, OrmLiteCriteria.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteGroupStandard.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteSchool.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteStandard.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteSubCriteria.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteSurvey.class, true);
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
            surveyDao = new SurveyDao(getConnectionSource(), OrmLiteSurvey.class);
        }
        return surveyDao;
    }

    public GroupStandardDao getGroupStandardDao() throws SQLException {
        if (groupStandardDao == null) {
            groupStandardDao = new GroupStandardDao(getConnectionSource(), OrmLiteGroupStandard.class);
        }
        return groupStandardDao;
    }

    public StandardDao getStandardDao() throws SQLException {
        if (standardDao == null) {
            standardDao = new StandardDao(getConnectionSource(), OrmLiteStandard.class);
        }
        return standardDao;
    }

    public CriteriaDao getCriteriaDao() throws SQLException {
        if (criteriaDao == null) {
            criteriaDao = new CriteriaDao(getConnectionSource(), OrmLiteCriteria.class);
        }
        return criteriaDao;
    }

    public SubCriteriaDao getSubCriteriaDao() throws SQLException {
        if (subCriteriaDao == null) {
            subCriteriaDao = new SubCriteriaDao(getConnectionSource(), OrmLiteSubCriteria.class);
        }
        return subCriteriaDao;
    }

    public OrmLiteAnswerDao getAnswerDao() throws SQLException {
        if (answerDao == null) {
            answerDao = new OrmLiteAnswerDao(getConnectionSource(), OrmLiteAnswer.class);
        }
        return answerDao;
    }
}
