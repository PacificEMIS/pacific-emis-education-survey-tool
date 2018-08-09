package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fm.doe.national.data.models.survey.GroupStandard;
import io.reactivex.Single;

public class GroupStandardDao extends BaseRxDao<GroupStandard, Long> {

    GroupStandardDao(ConnectionSource connectionSource, Class<GroupStandard> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<GroupStandard> createGroup() {
        return Single.fromCallable(() -> {
            GroupStandard groupStandard = new GroupStandard();
            create(groupStandard);
            return groupStandard;
        });
    }

}
