package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import fm.doe.national.data.models.survey.School;
import io.reactivex.Completable;
import io.reactivex.Single;

public class SchoolDao extends BaseRxDao<School, Long> {

    SchoolDao(ConnectionSource connectionSource, Class<School> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<School> createSchool(String schoolName) {
        return Single.fromCallable(() -> {
            School school = new School(schoolName);
            create(school);
            return school;
        });
    }

    public Completable addSchools(List<School> schools) {
        return Completable.fromAction(() -> {
            for (School school : schools) {
                createOrUpdate(new School(school.getId(), school.getName()));
            }
        });
    }

}
