package fm.doe.national.data.data_source.db;


import android.util.Pair;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.db.dao.AnswerDao;
import fm.doe.national.data.data_source.db.dao.DatabaseHelper;
import fm.doe.national.data.data_source.db.dao.SchoolDao;
import fm.doe.national.data.data_source.db.dao.SurveyDao;
import fm.doe.national.data.data_source.db.dao.SurveyItemDao;
import fm.doe.national.data.data_source.db.dao.SurveyResultDao;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.SchoolAccreditationResult;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.db.OrmLiteAnswer;
import fm.doe.national.data.data_source.models.db.OrmLiteBaseSurveyResult;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteSchoolAccreditation;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteSchoolAccreditationResult;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteSubCriteria;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

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

    public Completable addSchools(List<School> schoolList) {
        return schoolDao.addSchools(schoolList);
    }

    @Override
    public Single<List<School>> requestSchools() {
        return schoolDao.getAllQueriesSingle()
                .map(ArrayList<School>::new);
    }

    @Override
    public Single<Answer> createAnswer(boolean answer, SubCriteria criteria, SchoolAccreditationResult result) {
        return surveyItemDao
                // FIXME: Possible Name collision
                .requestItemByName(criteria.getName())
                .flatMap(criteriaItem -> surveyResultDao
                        .requestSurveyResult(result.getStartDate())
                        .flatMap(resultItem -> answerDao.createAnswer(answer, criteriaItem, resultItem)));

    }

    @Override
    public Single<Answer> requestAnswer(SubCriteria subCriteria, SchoolAccreditationResult result) {
        OrmLiteSurveyItem surveyItem = ((OrmLiteSubCriteria) subCriteria).getSurveyItem();
        OrmLiteBaseSurveyResult surveyResult = ((OrmLiteSchoolAccreditationResult) subCriteria).getSurveyResult();

        return answerDao.requestAnswer(surveyItem, surveyResult)
                .map(answer -> answer);
    }

    @Override
    public Single<Map<SubCriteria, Answer>> requestAnswers(Criteria criteria, SchoolAccreditationResult result) {
        return Observable.fromIterable(criteria.getSubCriterias())
                .concatMap(subCriteria -> requestAnswer(subCriteria, result)
                        .map(children -> Pair.create(subCriteria, children))
                        .toObservable())
                .toMap(pair -> pair.first, pair -> pair.second);

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
                .map(ArrayList<SchoolAccreditationResult>::new);
    }

}
