package fm.doe.national.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import fm.doe.national.data_source.db.models.OrmLiteSchool;
import fm.doe.national.models.survey.School;
import io.reactivex.Completable;
import io.reactivex.Single;

public class SchoolDao extends BaseRxDao<OrmLiteSchool, Long> {

    SchoolDao(ConnectionSource connectionSource, Class<OrmLiteSchool> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteSchool> createSchool(String schoolName) {
        return Single.fromCallable(() -> {
            OrmLiteSchool school = new OrmLiteSchool(schoolName);
            int id = create(school);
            return school;
        });
    }

    public Completable addSchools(List<School> schools) {
        return Completable.fromAction(() -> {
            for (School school : schools) {
                createOrUpdate(new OrmLiteSchool(school.getId(), school.getName()));
            }
        });
    }

}
