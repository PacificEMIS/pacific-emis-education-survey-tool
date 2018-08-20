package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import io.reactivex.Single;

class BaseRxDao<T, ID> extends BaseDaoImpl<T, ID> {

    BaseRxDao(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<Integer> createSingle(T data) {
        return Single.fromCallable(() -> create(data));
    }

    public Single<Integer> updateSingle(T data) {
        return Single.fromCallable(() -> update(data));
    }

    public Single<List<T>> getAllQueriesSingle() {
        return Single.fromCallable(this::queryForAll);
    }

}
