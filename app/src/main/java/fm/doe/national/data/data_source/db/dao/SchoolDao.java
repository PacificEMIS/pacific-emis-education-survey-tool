package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import fm.doe.national.data.data_source.models.survey.db.OrmLiteSchool;
import io.reactivex.Completable;
import io.reactivex.Single;

public class SchoolDao extends BaseRxDao<OrmLiteSchool, Long> {

    SchoolDao(ConnectionSource connectionSource, Class<OrmLiteSchool> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteSchool> createSchool(String schoolName) {
        return Single.fromCallable(() -> {
            OrmLiteSchool school = new OrmLiteSchool(schoolName);
            create(school);
            return school;
        });
    }

    public Completable addSchools(List<OrmLiteSchool> schools) {
        return Completable.fromAction(() -> {
            for (OrmLiteSchool school : schools) {
                createOrUpdate(new OrmLiteSchool(school.getId(), school.getName()));
            }
        });
    }

}
