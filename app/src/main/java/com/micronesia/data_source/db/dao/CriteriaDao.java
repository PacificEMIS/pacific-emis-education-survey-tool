package com.micronesia.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;
import com.micronesia.data_source.db.models.OrmLiteCriteria;
import com.micronesia.data_source.db.models.OrmLiteStandard;

import java.sql.SQLException;

import io.reactivex.Single;

public class CriteriaDao extends BaseRxDao<OrmLiteCriteria, Long> {

    CriteriaDao(ConnectionSource connectionSource, Class<OrmLiteCriteria> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteCriteria> createCriteria(String name, OrmLiteStandard standard) {
        return Single.fromCallable(() -> {
            OrmLiteCriteria criteria = new OrmLiteCriteria(name, standard);
            int id = create(criteria);
            return criteria;
        });
    }

}
