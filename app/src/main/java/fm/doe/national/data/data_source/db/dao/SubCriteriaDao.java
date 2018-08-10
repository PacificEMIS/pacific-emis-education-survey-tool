package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fm.doe.national.data.data_source.db.models.survey.OrmLiteCriteria;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteSubCriteria;
import io.reactivex.Single;

public class SubCriteriaDao extends BaseRxDao<OrmLiteSubCriteria, Long> {

    protected SubCriteriaDao(ConnectionSource connectionSource, Class<OrmLiteSubCriteria> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteSubCriteria> createSubCriteria(String name, OrmLiteCriteria criteria) {
        return Single.fromCallable(() -> {
            OrmLiteSubCriteria subCriteria = new OrmLiteSubCriteria(name, criteria);
            create(subCriteria);
            return subCriteria;
        });
    }

}
