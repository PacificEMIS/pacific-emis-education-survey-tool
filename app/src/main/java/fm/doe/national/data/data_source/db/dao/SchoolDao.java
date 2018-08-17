package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.db.OrmLiteSchool;
import io.reactivex.Completable;
import io.reactivex.Single;

public class SchoolDao extends BaseRxDao<OrmLiteSchool, Long> {

    SchoolDao(ConnectionSource connectionSource, Class<OrmLiteSchool> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Single<OrmLiteSchool> createSchool(String schoolName, String id) {
        return Single.fromCallable(() -> {
            OrmLiteSchool school = new OrmLiteSchool(id, schoolName);
            create(school);
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

    public Single<OrmLiteSchool> requestSchool(String schoolId) {
        return Single.fromCallable(() -> queryBuilder()
                .where()
                .eq(OrmLiteSchool.Column.ID, schoolId)
                .queryForFirst());
    }

}
