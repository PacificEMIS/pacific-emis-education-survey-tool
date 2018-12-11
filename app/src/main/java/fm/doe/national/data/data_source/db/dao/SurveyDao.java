package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import fm.doe.national.data.data_source.models.db.OrmLiteSurvey;
import fm.doe.national.data.data_source.models.serializable.LinkedCategory;
import io.reactivex.Completable;

public class SurveyDao extends BaseRxDao<OrmLiteSurvey, Long> {

    private SurveyItemDao surveyItemDao;

    SurveyDao(SurveyItemDao surveyItemDao,
              ConnectionSource connectionSource,
              Class<OrmLiteSurvey> dataClass) throws SQLException {
        super(connectionSource, dataClass);
        this.surveyItemDao = surveyItemDao;
    }

    public Completable createSchoolAccreditation(int version,
                                                 String type,
                                                 List<? extends LinkedCategory> categories) {
        return Completable.fromCallable(() -> {
            OrmLiteSurvey ormLiteSurvey = new OrmLiteSurvey(version, type);
            createIfNotExists(ormLiteSurvey);
            surveyItemDao.createFromCategories(categories, ormLiteSurvey);

            return  ormLiteSurvey;
        });
    }

    public OrmLiteSurvey getRelevantSurvey() throws SQLException {
        return queryBuilder()
                .orderBy(OrmLiteSurvey.Column.VERSION, false)
                .queryForFirst();
    }

}