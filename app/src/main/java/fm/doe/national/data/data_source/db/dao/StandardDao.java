package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fm.doe.national.data.data_source.models.survey.db.OrmLiteCriteria;
import fm.doe.national.data.data_source.models.survey.db.OrmLiteGroupStandard;
import fm.doe.national.data.data_source.models.survey.db.OrmLiteStandard;
import fm.doe.national.data.data_source.models.survey.Standard;
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

    public List<OrmLiteStandard> createStandards(DatabaseHelper helper, OrmLiteGroupStandard groupStandard, Collection<? extends Standard> standards) throws SQLException {
        List<OrmLiteStandard> ormLiteStandards = new ArrayList<>(standards.size());
        for (Standard standard : standards) {
            OrmLiteStandard ormLiteStandard = new OrmLiteStandard(standard.getName(), groupStandard);
            List<OrmLiteCriteria> ormLiteCriteriaList = helper.getCriteriaDao().fill(helper, ormLiteStandard, standard.getCriterias());
            ormLiteStandard.addCriterias(ormLiteCriteriaList);

            ormLiteStandards.add(ormLiteStandard);
        }
        return ormLiteStandards;
    }
}
