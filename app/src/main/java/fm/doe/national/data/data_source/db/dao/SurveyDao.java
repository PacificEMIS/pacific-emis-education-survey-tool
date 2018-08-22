package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collection;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.db.OrmLiteBaseSurvey;
import io.reactivex.Completable;
import io.reactivex.Single;

public class SurveyDao extends BaseRxDao<OrmLiteBaseSurvey, Long> {

    private SurveyItemDao surveyItemDao;

    SurveyDao(SurveyItemDao surveyItemDao,
              ConnectionSource connectionSource,
              Class<OrmLiteBaseSurvey> dataClass) throws SQLException {
        super(connectionSource, dataClass);
        this.surveyItemDao = surveyItemDao;
    }

    public Single<OrmLiteBaseSurvey> createSchoolAccreditation(int version, String type) {
        return Single.fromCallable(() -> new OrmLiteBaseSurvey(version, type));
    }

    public Completable createSchoolAccreditation(int version,
                                                 String type,
                                                 Collection<? extends GroupStandard> groupStandards) {
        return Single.fromCallable(() -> new OrmLiteBaseSurvey(version, type))
                .map(this::createIfNotExists)
                .map(survey -> surveyItemDao.createFromGroupStandards(groupStandards, survey))
                .ignoreElement();
    }

    public OrmLiteBaseSurvey getRelevantSurvey() throws SQLException {
        return queryBuilder().orderBy(OrmLiteBaseSurvey.Column.VERSION, false).queryForFirst();
    }

}