package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fm.doe.national.data.data_source.db.models.survey.OrmLiteCriteria;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteStandard;
import io.reactivex.Single;

public class CriteriaDao extends BaseRxDao<OrmLiteCriteria, Long> {

    CriteriaDao(ConnectionSource connectionSource, Class<OrmLiteCriteria> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteCriteria> createCriteria(String name, OrmLiteStandard standard) {
        return Single.fromCallable(() -> {
            OrmLiteCriteria criteria = new OrmLiteCriteria(name, standard);
            create(criteria);
            return criteria;
        });
    }

}
