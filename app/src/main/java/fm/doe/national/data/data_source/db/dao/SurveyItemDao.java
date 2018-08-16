package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import fm.doe.national.data.converters.JsonObjectsContainer;
import fm.doe.national.data.data_source.models.db.OrmLiteGroupStandard;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import fm.doe.national.data.data_source.models.serializable.SerializableGroupStandard;

public class SurveyItemDao extends BaseRxDao<OrmLiteSurveyItem, Long> {

    SurveyItemDao(ConnectionSource connectionSource, Class<OrmLiteSurveyItem> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<OrmLiteGroupStandard> createGroupStandards(DatabaseHelper helper, JsonObjectsContainer<SerializableGroupStandard> jsonObjectsContainer) throws
            SQLException {
        return null;
    }
}
