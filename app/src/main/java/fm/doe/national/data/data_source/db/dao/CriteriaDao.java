package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fm.doe.national.data.data_source.models.survey.db.OrmLiteCriteria;
import fm.doe.national.data.data_source.models.survey.db.OrmLiteStandard;
import fm.doe.national.data.data_source.models.survey.Criteria;
import io.reactivex.Single;

public class CriteriaDao extends BaseRxDao<OrmLiteCriteria, Long> {

    CriteriaDao(ConnectionSource connectionSource, Class<OrmLiteCriteria> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteCriteria> createCriteria(String name, OrmLiteStandard standard) {
        return Single.fromCallable(() -> {
            OrmLiteCriteria criteria = new OrmLiteCriteria(name, standard);
            create(criteria);
            return criteria;
        });
    }

    public List<OrmLiteCriteria> fill(DatabaseHelper helper, OrmLiteStandard standard, Collection<? extends Criteria> criterias)
            throws
            SQLException {
        List<OrmLiteCriteria> ormLiteCriterias = new ArrayList<>();
        for (Criteria criteria : criterias) {
            OrmLiteCriteria ormLiteCriteria = new OrmLiteCriteria(
                    criteria.getName(),
                    standard);
            ormLiteCriteria.setSubCriterias(helper.getSubCriteriaDao().createSubCriterias(ormLiteCriteria, criteria.getSubCriterias()));
            create(ormLiteCriteria);

            ormLiteCriterias.add(ormLiteCriteria);
        }
        return ormLiteCriterias;
    }
}
