package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fm.doe.national.data.models.survey.Criteria;
import fm.doe.national.data.models.survey.Standard;
import io.reactivex.Single;

public class CriteriaDao extends BaseRxDao<Criteria, Long> {

    CriteriaDao(ConnectionSource connectionSource, Class<Criteria> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<Criteria> createCriteria(String name, Standard standard) {
        return Single.fromCallable(() -> {
            Criteria criteria = new Criteria(name, standard);
            create(criteria);
            return criteria;
        });
    }

}
