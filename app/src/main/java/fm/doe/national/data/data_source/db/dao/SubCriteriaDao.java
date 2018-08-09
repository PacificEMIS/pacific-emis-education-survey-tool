package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fm.doe.national.data.models.survey.Criteria;
import fm.doe.national.data.models.survey.SubCriteria;
import io.reactivex.Single;

public class SubCriteriaDao extends BaseRxDao<SubCriteria, Long> {

    protected SubCriteriaDao(ConnectionSource connectionSource, Class<SubCriteria> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<SubCriteria> createSubCriteria(String name, Criteria criteria) {
        return Single.fromCallable(() -> {
            SubCriteria subCriteria = new SubCriteria(name, criteria);
            create(subCriteria);
            return subCriteria;
        });
    }

}
