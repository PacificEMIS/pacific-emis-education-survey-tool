package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fm.doe.national.data.models.survey.GroupStandard;
import fm.doe.national.data.models.survey.Standard;
import io.reactivex.Single;

public class StandardDao extends BaseRxDao<Standard, Long> {

    StandardDao(ConnectionSource connectionSource, Class<Standard> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<Standard> createStandard(String name, GroupStandard groupStandard) {
        return Single.fromCallable(() -> {
            Standard standard = new Standard(name, groupStandard);
            create(standard);
            return standard;
        });
    }

}
