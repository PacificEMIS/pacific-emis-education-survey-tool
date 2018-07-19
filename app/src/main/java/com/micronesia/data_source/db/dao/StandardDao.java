package com.micronesia.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;
import com.micronesia.data_source.db.models.OrmLiteGroupStandard;
import com.micronesia.data_source.db.models.OrmLiteStandard;

import java.sql.SQLException;

import io.reactivex.Single;

public class StandardDao extends BaseRxDao<OrmLiteStandard, Long> {

    StandardDao(ConnectionSource connectionSource, Class<OrmLiteStandard> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteStandard> createStandard(String name, OrmLiteGroupStandard groupStandard) {
        return Single.fromCallable(() -> {
            OrmLiteStandard standard = new OrmLiteStandard(name, groupStandard);
            int id = create(standard);
            return standard;
        });
    }

}
