package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Date;

import fm.doe.national.data.data_source.models.db.OrmLiteSchool;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyPassing;
import io.reactivex.Single;

public class SurveyPassingDao extends BaseRxDao<OrmLiteSurveyPassing, Long> {

    private SurveyDao surveyDao;

    SurveyPassingDao(SurveyDao surveyDao,
                     ConnectionSource connectionSource,
                     Class<OrmLiteSurveyPassing> dataClass) throws SQLException {
        super(connectionSource, dataClass);
        this.surveyDao = surveyDao;
    }

    public Single<OrmLiteSurveyPassing> createSurveyPassing(Date startDate, OrmLiteSchool school) throws SQLException {
        return Single
                .just(new OrmLiteSurveyPassing(startDate, school, surveyDao.getRelevantSurvey()))
                .map(this::createIfNotExists);
    }

    public Single<OrmLiteSurveyPassing> requestSurveyPassing(Date startDate) {
        return Single.fromCallable(() -> queryBuilder()
                        .where()
                        .eq(OrmLiteSurveyPassing.Column.START_DATE, startDate)
                        .queryForFirst());
    }

}
