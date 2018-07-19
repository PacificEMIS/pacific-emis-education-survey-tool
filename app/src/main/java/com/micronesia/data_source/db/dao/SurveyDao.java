package com.micronesia.data_source.db.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.micronesia.data_source.db.models.OrmLiteSchool;
import com.micronesia.data_source.db.models.OrmLiteSurvey;
import com.micronesia.models.survey.School;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import io.reactivex.Single;

public class SurveyDao extends BaseDaoImpl<OrmLiteSurvey, Long> {

    SurveyDao(ConnectionSource connectionSource, Class<OrmLiteSurvey> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteSurvey> createSurvey(OrmLiteSchool school, int year) {
        return Single.fromCallable(() -> {
            OrmLiteSurvey survey = new OrmLiteSurvey(year, school);
            int id = create(survey);
            return survey;
        });
    }


}
