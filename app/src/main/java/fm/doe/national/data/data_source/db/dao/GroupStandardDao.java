package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.converters.JsonObjectsContainer;
import fm.doe.national.data.data_source.models.survey.db.OrmLiteGroupStandard;
import fm.doe.national.data.data_source.models.survey.db.OrmLiteStandard;
import fm.doe.national.data.data_source.models.survey.serializable.SerializableGroupStandard;
import io.reactivex.Single;

public class GroupStandardDao extends BaseRxDao<OrmLiteGroupStandard, Long> {

    GroupStandardDao(ConnectionSource connectionSource, Class<OrmLiteGroupStandard> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteGroupStandard> createGroup() {
        return Single.fromCallable(() -> {
            OrmLiteGroupStandard groupStandard = new OrmLiteGroupStandard(this);
            create(groupStandard);
            return groupStandard;
        });
    }

    public List<OrmLiteGroupStandard> createGroupStandards(DatabaseHelper helper, JsonObjectsContainer<SerializableGroupStandard> jsonObjectsContainer) throws
            SQLException {
        List<OrmLiteGroupStandard> ormLiteGroupStandards = new ArrayList<>();
        for (SerializableGroupStandard groupStandard : jsonObjectsContainer.getObjects()) {
            OrmLiteGroupStandard ormLiteGroupStandard = new OrmLiteGroupStandard();
            List<OrmLiteStandard> ormLiteStandards = helper.getStandardDao().createStandards(helper, ormLiteGroupStandard, groupStandard.getStandards());

            create(ormLiteGroupStandard);
            ormLiteGroupStandard.addStandards(ormLiteStandards);
            ormLiteGroupStandards.add(ormLiteGroupStandard);
        }

        return ormLiteGroupStandards;
    }
}
