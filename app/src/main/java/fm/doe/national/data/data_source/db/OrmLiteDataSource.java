package fm.doe.national.data.data_source.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.db.dao.AnswerDao;
import fm.doe.national.data.data_source.db.dao.CategoryProgressDao;
import fm.doe.national.data.data_source.db.dao.DatabaseHelper;
import fm.doe.national.data.data_source.db.dao.SchoolDao;
import fm.doe.national.data.data_source.db.dao.SurveyDao;
import fm.doe.national.data.data_source.db.dao.SurveyItemDao;
import fm.doe.national.data.data_source.db.dao.SurveyPassingDao;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteCriteria;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteGroupStandard;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteSchoolAccreditation;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteSchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteStandard;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteSubCriteria;
import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class OrmLiteDataSource implements DataSource {

    private SchoolDao schoolDao;
    private SurveyDao surveyDao;
    private SurveyItemDao surveyItemDao;
    private SurveyPassingDao surveyPassingDao;
    private CategoryProgressDao categoryProgressDao;
    private AnswerDao answerDao;

    public OrmLiteDataSource(DatabaseHelper helper) {
        try {
            schoolDao = helper.getSchoolDao();
            surveyDao = helper.getSurveyDao();
            surveyItemDao = helper.getSurveyItemDao();
            answerDao = helper.getAnswerDao();
            surveyPassingDao = helper.getSurveyPassingDao();
            categoryProgressDao = helper.getCategoryProgressDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public Completable updateAnswer(long passingId, long subCriteriaId, Answer.State state) {
        return Completable.fromSingle(surveyPassingDao.getItemSingle(passingId)
                .flatMap(passing -> surveyItemDao.requestItem(subCriteriaId)
                        .flatMap(subCriteriaItem -> answerDao.updateAnswer(subCriteriaItem, passing, state)
                                .flatMap(answer -> categoryProgressDao.updateCategoryProgress(subCriteriaItem.getParentItem(), passing, state)))));
    }

    @Override
    public Completable createSchoolAccreditation(LinkedSchoolAccreditation schoolAccreditation) {
        return surveyDao.createSchoolAccreditation(
                schoolAccreditation.getVersion(),
                schoolAccreditation.getType(),
                schoolAccreditation.getGroupStandards());
    }

    @Override
    public Single<LinkedSchoolAccreditation> requestLinkedSchoolAccreditation() {
        // For future export feature
        return null;
    }

    @Override
    public Single<SchoolAccreditationPassing> createNewSchoolAccreditationPassing(int year, School school) {
        return schoolDao.requestSchool(school.getId())
                .flatMap(schoolItem -> surveyPassingDao.createSurveyPassing(year, schoolItem)
                        .flatMap(surveyPassing -> categoryProgressDao.requestCategoryProgress(
                                surveyPassing,
                                surveyPassing.getSurvey().getSurveyItems())
                                .map(progress -> new OrmLiteSchoolAccreditation(surveyPassing.getSurvey(), progress))
                                .map(schoolAccreditation -> new OrmLiteSchoolAccreditationPassing(
                                        surveyPassing,
                                        schoolAccreditation))));
    }

    @Override
    public Single<List<SchoolAccreditationPassing>> requestSchoolAccreditationPassings() {
        return surveyPassingDao.getAllQueriesSingle()
                .toObservable()
                .flatMapIterable(resultList -> resultList)
                .flatMap(surveyPassing -> categoryProgressDao.requestCategoryProgress(surveyPassing, surveyPassing.getSurvey().getSurveyItems())
                        .map(progress -> new OrmLiteSchoolAccreditationPassing(
                                surveyPassing,
                                new OrmLiteSchoolAccreditation(surveyPassing.getSurvey(), progress)))
                        .toObservable())
                .toList()
                .map(ArrayList<SchoolAccreditationPassing>::new);
    }

    @Override
    public Single<List<GroupStandard>> requestGroupStandards(long passingId) {
        return surveyPassingDao.getItemSingle(passingId)
                .flatMap(passing -> Observable.fromIterable(passing.getSurvey().getSurveyItems())
                        .flatMap(surveyItem -> categoryProgressDao.requestCategoryProgress(passing, surveyItem)
                                .map(progress -> new OrmLiteGroupStandard(surveyItem, progress))
                                .toObservable())
                        .toList()
                        .map(ArrayList<GroupStandard>::new));
    }

    @Override
    public Single<List<Standard>> requestStandards(long passingId, long groupStandardId) {
        return surveyPassingDao.getItemSingle(passingId)
                .flatMap(passing -> surveyItemDao.requestItem(groupStandardId)
                        .flatMap(groupStandardItem -> Observable.fromIterable(groupStandardItem.getChildrenItems())
                                .flatMap(standardItem -> categoryProgressDao.requestCategoryProgress(passing, standardItem)
                                        .map(progress -> new OrmLiteStandard(standardItem, progress))
                                        .toObservable())
                                .toList()
                                .map(ArrayList<Standard>::new)));
    }

    @Override
    public Single<List<Criteria>> requestCriterias(long passingId, long standardId) {
        return surveyPassingDao.getItemSingle(passingId)
                .flatMap(passing -> surveyItemDao.requestItem(standardId)
                        .flatMap(standardItem -> Observable.fromIterable(standardItem.getChildrenItems())
                                .flatMap(criteriaItem -> Observable.zip(
                                        categoryProgressDao.requestCategoryProgress(passing, criteriaItem)
                                                .toObservable(),
                                        Observable.fromIterable(criteriaItem.getChildrenItems())
                                                .flatMap(subcriteriaItem -> answerDao.requestAnswer(subcriteriaItem, passing)
                                                        .map(answer -> new OrmLiteSubCriteria(subcriteriaItem, answer))
                                                        .toObservable())
                                                .toList()
                                                .toObservable(),
                                        (progress, subCriterias) -> new OrmLiteCriteria(criteriaItem, subCriterias, progress)))
                                .toList())
                        .map(ArrayList<Criteria>::new));
    }

}
