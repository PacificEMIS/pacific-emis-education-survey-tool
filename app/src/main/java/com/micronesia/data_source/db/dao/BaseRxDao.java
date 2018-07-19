package com.micronesia.data_source.db.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Single;

class BaseRxDao<T, ID> extends BaseDaoImpl<T, ID> {

    BaseRxDao(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<Integer> createData(T data) {
        return Single.fromCallable(() -> create(data));
    }

    public Single<Integer> updateData(T data) {
        return Single.fromCallable(() -> update(data));
    }

    public Single<List<T>> getAllQueriesSingle() {
        return Single.fromCallable(this::queryForAll);
    }

}
