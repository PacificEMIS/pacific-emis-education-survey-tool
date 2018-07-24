package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fm.doe.national.data.data_source.db.models.OrmLiteGroupStandard;
import io.reactivex.Single;

public class GroupStandardDao extends BaseRxDao<OrmLiteGroupStandard, Long> {

    GroupStandardDao(ConnectionSource connectionSource, Class<OrmLiteGroupStandard> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteGroupStandard> createGroup() {
        return Single.fromCallable(() -> {
            OrmLiteGroupStandard groupStandard = new OrmLiteGroupStandard();
            create(groupStandard);
            return groupStandard;
        });
    }

}
