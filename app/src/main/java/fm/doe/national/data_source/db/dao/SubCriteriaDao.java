package fm.doe.national.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fm.doe.national.data_source.db.models.OrmLiteCriteria;
import fm.doe.national.data_source.db.models.OrmLiteSubCriteria;
import io.reactivex.Single;

public class SubCriteriaDao extends BaseRxDao<OrmLiteSubCriteria, Long> {

    protected SubCriteriaDao(ConnectionSource connectionSource, Class<OrmLiteSubCriteria> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteSubCriteria> createSubCriteria(String name, String question, OrmLiteCriteria criteria) {
        return Single.fromCallable(() -> {
            OrmLiteSubCriteria subCriteria = new OrmLiteSubCriteria(name, question, criteria);
            int id = create(subCriteria);
            return subCriteria;
        });
    }

}
