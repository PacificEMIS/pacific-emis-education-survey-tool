package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SurveyPassing;
import fm.doe.national.data.data_source.models.db.OrmLiteSchool;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyPassing;
import io.reactivex.Completable;
import io.reactivex.Single;

public class SchoolDao extends BaseRxDao<OrmLiteSchool, Long> {

    private static final int NOT_FOUND = -1;
    private final SurveyPassingDao surveyPassingDao;

    SchoolDao(SurveyPassingDao surveyPassingDao, ConnectionSource connectionSource, Class<OrmLiteSchool> dataClass) throws SQLException {
        super(connectionSource, dataClass);
        this.surveyPassingDao = surveyPassingDao;
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

    public Completable updateSchools(List<School> schools) {
        return Completable.fromAction(() -> {
            List<OrmLiteSchool> currentSchools = queryForAll();
            List<School> newSchools = new ArrayList<>(schools);

            for (OrmLiteSchool school : currentSchools) {
                int index = newSchools.indexOf(school);
                if (index != NOT_FOUND) {
                    updateSchool(school, newSchools.get(index));
                    newSchools.remove(school);
                } else {
                    deleteSchool(school);
                }
            }

            for (School school : newSchools) {
                create(new OrmLiteSchool(school.getId(), school.getName()));
            }
        });
    }

    private void updateSchool(OrmLiteSchool school, School newSchool) throws SQLException {
        school.setName(newSchool.getName());
        update(school);
    }

    private void deleteSchool(OrmLiteSchool school) throws SQLException {
        DeleteBuilder<OrmLiteSurveyPassing, Long> deleteBuilder = surveyPassingDao.deleteBuilder();
        deleteBuilder
                .where()
                .eq(OrmLiteSurveyPassing.Column.SCHOOL, school);
        deleteBuilder.delete();

        delete(school);
    }

    public Single<OrmLiteSchool> requestSchool(String schoolId) {
        return Single.fromCallable(() -> queryBuilder()
                .where()
                .eq(OrmLiteSchool.Column.ID, schoolId)
                .queryForFirst());
    }

}
