package fm.doe.national.data.data_source.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.db.dao.AnswerDao;
import fm.doe.national.data.data_source.db.dao.CriteriaDao;
import fm.doe.national.data.data_source.db.dao.DatabaseHelper;
import fm.doe.national.data.data_source.db.dao.GroupStandardDao;
import fm.doe.national.data.data_source.db.dao.SchoolDao;
import fm.doe.national.data.data_source.db.dao.StandardDao;
import fm.doe.national.data.data_source.db.dao.SubCriteriaDao;
import fm.doe.national.data.data_source.db.dao.SurveyDao;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteAnswer;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteCriteria;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteGroupStandard;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteSchool;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteStandard;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteSubCriteria;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteSurvey;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class DatabaseDataSource implements DataSource {

    private SchoolDao schoolDao;
    private SurveyDao surveyDao;
    private GroupStandardDao groupStandardDao;
    private StandardDao standardDao;
    private CriteriaDao criteriaDao;
    private SubCriteriaDao subCriteriaDao;
    private AnswerDao answerDao;

    public DatabaseDataSource(DatabaseHelper helper) {
        try {
            schoolDao = helper.getSchoolDao();
            surveyDao = helper.getSurveyDao();
            groupStandardDao = helper.getGroupStandardDao();
            standardDao = helper.getStandardDao();
            criteriaDao = helper.getCriteriaDao();
            subCriteriaDao = helper.getSubCriteriaDao();
            answerDao = helper.getAnswerDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Single<OrmLiteSchool> createSchool(String name) {
        return schoolDao.createSchool(name)
                .map(school -> school);
    }

    public Completable addSchools(List<OrmLiteSchool> schoolList) {
        return schoolDao.addSchools(schoolList);
    }

    @Override
    public Single<List<OrmLiteSchool>> requestSchools() {
        return schoolDao.getAllQueriesSingle()
                .map(ArrayList<OrmLiteSchool>::new);
    }

    @Override
    public Single<OrmLiteSurvey> createSurvey(OrmLiteSchool school, int year) {
        OrmLiteSchool ormLiteSchool = (OrmLiteSchool) school;
        return surveyDao.createSurvey(ormLiteSchool, year)
                .map(survey -> survey);
    }

    @Override
    public Single<OrmLiteGroupStandard> createGroupStandard() {
        return groupStandardDao.createGroup()
                .map(groupStandard -> groupStandard);
    }

    @Override
    public Single<List<OrmLiteGroupStandard>> requestGroupStandard() {
        return Single.fromCallable(() -> groupStandardDao.queryForAll())
                .map(ArrayList<OrmLiteGroupStandard>::new);
    }

    @Override
    public Single<OrmLiteStandard> createStandard(String name, OrmLiteGroupStandard group) {
        return standardDao.createStandard(name, (OrmLiteGroupStandard) group)
                .map(standard -> standard);
    }

    @Override
    public Single<OrmLiteCriteria> createCriteria(String name, OrmLiteStandard standard) {
        return criteriaDao.createCriteria(name, (OrmLiteStandard) standard)
                .map((Function<OrmLiteCriteria, OrmLiteCriteria>) criteria -> criteria);
    }

    @Override
    public Single<OrmLiteSubCriteria> createSubCriteria(String name, OrmLiteCriteria criteria) {
        return subCriteriaDao.createSubCriteria(name, (OrmLiteCriteria) criteria)
                .map(subCriteria -> subCriteria);
    }

    @Override
    public Single<OrmLiteAnswer> createAnswer(boolean answer, OrmLiteSubCriteria criteria, OrmLiteSurvey survey) {
        return answerDao.createAnswer(answer, (OrmLiteSubCriteria) criteria, (OrmLiteSurvey) survey)
                .map(answer1 -> answer1);
    }

    public Completable updateAnswer(OrmLiteAnswer answer) {
        return answerDao.updateAnswer((OrmLiteAnswer) answer);
    }

    public Single<List<OrmLiteAnswer>> requestAnswers(OrmLiteSurvey survey) {
        return answerDao.getAnswers(survey)
                .map(ArrayList<OrmLiteAnswer>::new);
    }

}
