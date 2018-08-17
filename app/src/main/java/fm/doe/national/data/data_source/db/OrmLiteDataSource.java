package fm.doe.national.data.data_source.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.db.dao.AnswerDao;
import fm.doe.national.data.data_source.db.dao.DatabaseHelper;
import fm.doe.national.data.data_source.db.dao.SchoolDao;
import fm.doe.national.data.data_source.db.dao.SurveyDao;
import fm.doe.national.data.data_source.db.dao.SurveyItemDao;
import fm.doe.national.data.data_source.db.dao.SurveyResultDao;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.SchoolAccreditationResult;
import fm.doe.national.data.data_source.models.db.OrmLiteAnswer;
import fm.doe.national.data.data_source.models.db.OrmLiteCriteria;
import fm.doe.national.data.data_source.models.db.OrmLiteGroupStandard;
import fm.doe.national.data.data_source.models.db.OrmLiteSchool;
import fm.doe.national.data.data_source.models.db.OrmLiteSchoolAccreditation;
import fm.doe.national.data.data_source.models.db.OrmLiteSchoolAccreditationResult;
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
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class OrmLiteDataSource implements DataSource {

    private SchoolDao schoolDao;
    private SurveyDao surveyDao;
    private SurveyItemDao surveyItemDao;
    private SurveyResultDao surveyResultDao;
    private AnswerDao answerDao;

    public OrmLiteDataSource(DatabaseHelper helper) {
        try {
            schoolDao = helper.getSchoolDao();
            surveyDao = helper.getSurveyDao();
            surveyItemDao = helper.getSurveyItemDao();
            answerDao = helper.getAnswerDao();
            surveyResultDao = helper.getSurveyResultDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Single<School> createSchool(String name, String id) {
        return schoolDao.createSchool(id, name)
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
    public Single<Answer> createAnswer(boolean answer, SubCriteria criteria, Survey survey) {
        return null;

    }

    @Override
    public Completable updateAnswer(Answer answer) {
        return answerDao.updateAnswer((OrmLiteAnswer) answer);
    }

    @Override
    public Single<SchoolAccreditation> createNewSchoolAccreditation(int year, School school) {
        return schoolDao.requestSchool(school.getId())
                .flatMap(schoolItem -> surveyResultDao.createSurveyResult(year, schoolItem)
                        .map(surveyResult -> new OrmLiteSchoolAccreditation(surveyResult.getSurvey())));
    }

    @Override
    public Single<List<SchoolAccreditationResult>> requestSchoolAccreditationResults() {
        return surveyResultDao.getAllQueriesSingle().toObservable().flatMapIterable(resultList -> resultList)
                .map(OrmLiteSchoolAccreditationResult::new)
                .toList()
                .map(list -> {
                    List<SchoolAccreditationResult> schoolAccreditationResults = new ArrayList<>();
                    schoolAccreditationResults.addAll(list);
                    return schoolAccreditationResults;
        });
    }

    public Single<List<OrmLiteAnswer>> requestAnswers(OrmLiteBaseSurvey survey) {
        return answerDao.getAnswers(survey)
                .map(ArrayList::new);
    }

}
