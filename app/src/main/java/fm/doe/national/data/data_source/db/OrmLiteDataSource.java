package fm.doe.national.data.data_source.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.db.dao.AnswerDao;
import fm.doe.national.data.data_source.db.dao.DatabaseHelper;
import fm.doe.national.data.data_source.db.dao.SchoolDao;
import fm.doe.national.data.data_source.db.dao.SurveyDao;
import fm.doe.national.data.data_source.db.dao.SurveyItemDao;
import fm.doe.national.data.data_source.models.db.OrmLiteAnswer;
import fm.doe.national.data.data_source.models.db.OrmLiteCriteria;
import fm.doe.national.data.data_source.models.db.OrmLiteGroupStandard;
import fm.doe.national.data.data_source.models.db.OrmLiteSchool;
import fm.doe.national.data.data_source.models.db.OrmLiteStandard;
import fm.doe.national.data.data_source.models.db.OrmLiteBaseSurvey;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.Survey;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class OrmLiteDataSource implements DataSource {

    private SchoolDao schoolDao;
    private SurveyDao surveyDao;
    private SurveyItemDao surveyItemDao;
    private AnswerDao answerDao;

    public OrmLiteDataSource(DatabaseHelper helper) {
        try {
            schoolDao = helper.getSchoolDao();
            surveyDao = helper.getSurveyDao();
            surveyItemDao = helper.getSurveyItemDao();
//            standardDao = helper.getStandardDao();
//            criteriaDao = helper.getCriteriaDao();
//            subCriteriaDao = helper.getSubCriteriaDao();
//            answerDao = helper.getAnswerDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Single<School> createSchool(String name) {
        return schoolDao.createSchool(name)
                .map(school -> school);
    }

    public Completable addSchools(List<OrmLiteSchool> schoolList) {
        return schoolDao.addSchools(schoolList);
    }

    @Override
    public Single<List<School>> requestSchools() {
        return schoolDao.getAllQueriesSingle()
                .map(ArrayList<School>::new);
    }

    @Override
    public Single<Survey> createSurvey(School school, int year) {
        return null;
    }

    @Override
    public Single<GroupStandard> createGroupStandard() {
        return null;

    }

    @Override
    public Single<List<GroupStandard>> requestGroupStandard() {
        return surveyItemDao.getAllQueriesSingle().map(list -> {
            List<GroupStandard> groupStandards =new ArrayList<>();
            for (OrmLiteSurveyItem surveyItem : list) {
                groupStandards.add(new OrmLiteGroupStandard(surveyItem));
            }
            return groupStandards;
        });
    }

    @Override
    public Single<Standard> createStandard(String name, GroupStandard group) {
        return null;

    }

    @Override
    public Single<Criteria> createCriteria(String name, Standard standard) {
        return null;
    }

    @Override
    public Single<SubCriteria> createSubCriteria(String name, Criteria criteria) {
        return null;

    }

    @Override
    public Single<Answer> createAnswer(boolean answer, SubCriteria criteria, Survey survey) {
        return null;

    }

    @Override
    public Completable updateAnswer(Answer answer) {
        return answerDao.updateAnswer((OrmLiteAnswer) answer);
    }

    public Single<List<OrmLiteAnswer>> requestAnswers(OrmLiteBaseSurvey survey) {
        return answerDao.getAnswers(survey)
                .map(ArrayList::new);
    }

}
