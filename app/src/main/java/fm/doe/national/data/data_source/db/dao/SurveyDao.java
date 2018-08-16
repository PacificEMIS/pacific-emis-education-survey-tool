package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fm.doe.national.data.data_source.models.db.OrmLiteBaseSurvey;
import io.reactivex.Single;

public class SurveyDao extends BaseRxDao<OrmLiteBaseSurvey, Long> {

    SurveyDao(ConnectionSource connectionSource, Class<OrmLiteBaseSurvey> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteBaseSurvey> createSurvey(int version, String type) {
        return Single.fromCallable(() -> new OrmLiteBaseSurvey(version, type));
    }

}
