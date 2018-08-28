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
import fm.doe.national.data.data_source.db.dao.SurveyPassingDao;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.SurveyPassing;
import fm.doe.national.data.data_source.models.db.OrmLiteAnswer;
import fm.doe.national.data.data_source.models.db.OrmLiteSurveyItem;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteSchoolAccreditation;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteSchoolAccreditationPassing;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class OrmLiteDataSource implements DataSource {

    private SchoolDao schoolDao;
    private SurveyDao surveyDao;
    private SurveyItemDao surveyItemDao;
    private SurveyPassingDao surveyPassingDao;
    private AnswerDao answerDao;

    public OrmLiteDataSource(DatabaseHelper helper) {
        try {
            schoolDao = helper.getSchoolDao();
            surveyDao = helper.getSurveyDao();
            surveyItemDao = helper.getSurveyItemDao();
            answerDao = helper.getAnswerDao();
            surveyPassingDao = helper.getSurveyPassingDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Single<School> createSchool(String name, String id) {
        return schoolDao.createSchool(id, name)
                .map(school -> school);
    }

    @Override
    public Completable addSchools(List<School> schoolList) {
        return schoolDao.addSchools(schoolList);
    }

    @Override
    public Single<List<School>> requestSchools() {
        return schoolDao.getAllQueriesSingle()
                .map(ArrayList<School>::new);
    }

    @Override
    public Single<Answer> createAnswer(Answer.State state, SubCriteria criteria, SurveyPassing result) {
        return surveyItemDao
                .requestItem(criteria.getId())
                .flatMap(criteriaItem -> surveyPassingDao
                        .requestSurveyPassing(result.getStartDate())
                        .flatMap(resultItem -> answerDao.createAnswer(state, criteriaItem, resultItem)));

    }

    @Override
    public Completable updateAnswer(SubCriteria subCriteria) {
        return Completable.fromSingle(answerDao.updateAnswer((OrmLiteAnswer) subCriteria)
                .flatMap(updatedAnswer -> {
                    OrmLiteSurveyItem parentSurveyItem = updatedAnswer.getParentSurveyItem();
                    parentSurveyItem.incrementAnswersCount();
                    return surveyItemDao.updateSingle(parentSurveyItem);
                }));
    }

    @Override
    public Completable createSchoolAccreditation(SchoolAccreditation schoolAccreditation) {
        return surveyDao.createSchoolAccreditation(schoolAccreditation.getVersion(),
                schoolAccreditation.getType(),
                schoolAccreditation.getGroupStandards());
    }

    @Override
    public Single<SchoolAccreditation> createNewSchoolAccreditationPassing(int year, School school) {
        return schoolDao.requestSchool(school.getId())
                .flatMap(schoolItem -> surveyPassingDao.createSurveyPassing(year, schoolItem)
                        .map(surveyResult -> new OrmLiteSchoolAccreditation(surveyResult.getSurvey())));
    }

    @Override
    public Single<List<SchoolAccreditationPassing>> requestSchoolAccreditationPassings() {
        return surveyPassingDao.getAllQueriesSingle()
                .toObservable()
                .flatMapIterable(resultList -> resultList)
                .flatMap(item -> {
                    OrmLiteSchoolAccreditationPassing passing = new OrmLiteSchoolAccreditationPassing(item);
                    return Observable.zip(
                            Observable.fromIterable(passing
                                    .getSchoolAccreditation()
                                    .getGroupStandards())
                                    .flatMapIterable(GroupStandard::getStandards)
                                    .flatMapIterable(Standard::getCriterias)
                                    .flatMapIterable(Criteria::getSubCriterias)
                                    .concatMap(subCriteria -> surveyItemDao.requestItem(subCriteria.getId())
                                            .toObservable()
                                            .concatMap(ormLiteSurveyItem -> answerDao.requestAnswer(ormLiteSurveyItem, item)
                                                    .map(ormLiteAnswer -> {
                                                        subCriteria.setAnswer(ormLiteAnswer);
                                                        return subCriteria;
                                                    })
                                                    .toObservable())),
                            Observable.just(passing),
                            (subCriteria, passing2) -> passing);
                })
                .toList()
                .map(ArrayList<SchoolAccreditationPassing>::new);
    }

}
