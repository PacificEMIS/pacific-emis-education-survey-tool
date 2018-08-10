package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fm.doe.national.data.data_source.db.models.survey.OrmLiteGroupStandard;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteStandard;
import io.reactivex.Single;

public class StandardDao extends BaseRxDao<OrmLiteStandard, Long> {

    StandardDao(ConnectionSource connectionSource, Class<OrmLiteStandard> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteStandard> createStandard(String name, OrmLiteGroupStandard groupStandard) {
        return Single.fromCallable(() -> {
            OrmLiteStandard standard = new OrmLiteStandard(name, groupStandard);
            create(standard);
            return standard;
        });
    }

}
