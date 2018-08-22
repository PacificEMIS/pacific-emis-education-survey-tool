package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.db.OrmLiteSurvey;
import io.reactivex.Completable;
import io.reactivex.Single;

public class SurveyDao extends BaseRxDao<OrmLiteSurvey, Long> {

    private SurveyItemDao surveyItemDao;

    SurveyDao(SurveyItemDao surveyItemDao,
              ConnectionSource connectionSource,
              Class<OrmLiteSurvey> dataClass) throws SQLException {
        super(connectionSource, dataClass);
        this.surveyItemDao = surveyItemDao;
    }

    public Single<OrmLiteSurvey> createSchoolAccreditation(int version, String type) {
        return Single.fromCallable(() -> new OrmLiteSurvey(version, type));
    }

    public Completable createSchoolAccreditation(int version,
                                                 String type,
                                                 List<? extends GroupStandard> groupStandards) {
        return Completable.fromCallable(() -> {
            OrmLiteSurvey ormLiteSurvey = new OrmLiteSurvey(version, type);
            create(ormLiteSurvey);
            surveyItemDao.createFromGroupStandards(groupStandards, ormLiteSurvey);

            return  ormLiteSurvey;
        });
    }

    public OrmLiteSurvey getRelevantSurvey() throws SQLException {
        return queryBuilder()
                .orderBy(OrmLiteSurvey.Column.VERSION, false)
                .queryForFirst();
    }

}