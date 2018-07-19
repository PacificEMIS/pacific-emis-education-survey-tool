package fm.doe.national.data_source.db;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data_source.AccreditationDataSource;
import fm.doe.national.data_source.db.dao.*;
import fm.doe.national.data_source.db.models.*;
import fm.doe.national.models.survey.*;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class OrmLiteAccreditationDataSource implements AccreditationDataSource {

    private SchoolDao schoolDao;
    private SurveyDao surveyDao;
    private GroupStandardDao groupStandardDao;
    private StandardDao standardDao;
    private CriteriaDao criteriaDao;
    private SubCriteriaDao subCriteriaDao;
    private OrmLiteAnswerDao answerDao;

    public OrmLiteAccreditationDataSource(DatabaseHelper helper) {
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
    public Single<School> createSchool(String name) {
        return schoolDao.createSchool(name)
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
    public Single<Survey> createSurvey(School school, int year) {
        OrmLiteSchool ormLiteSchool = (OrmLiteSchool) school;
        return surveyDao.createSurvey(ormLiteSchool, year)
                .map(survey -> survey);
    }

    @Override
    public Single<GroupStandard> createGroupStandard() {
        return groupStandardDao.createGroup()
                .map(groupStandard -> groupStandard);
    }

    @Override
    public Single<Standard> createStandard(String name, GroupStandard group) {
        return standardDao.createStandard(name, (OrmLiteGroupStandard) group)
                .map(standard -> standard);
    }

    @Override
    public Single<Criteria> createCriteria(String name, Standard standard) {
        return criteriaDao.createCriteria(name, (OrmLiteStandard) standard)
                .map((Function<OrmLiteCriteria, OrmLiteCriteria>) criteria -> criteria);
    }

    @Override
    public Single<SubCriteria> createSubCriteria(String name, String question, Criteria criteria) {
        return subCriteriaDao.createSubCriteria(name, question, (OrmLiteCriteria) criteria)
                .map(subCriteria -> subCriteria);
    }

    @Override
    public Single<Answer> createAnswer(boolean answer, SubCriteria criteria, Survey survey) {
        return answerDao.createAnswer(answer, (OrmLiteSubCriteria) criteria, (OrmLiteSurvey) survey)
                .map(answer1 -> answer1);
    }

    public Completable updateAnswer(Answer answer) {
        return answerDao.updateAnswer((OrmLiteAnswer) answer);
    }

    public Single<List<Answer>> requestAnswers(Survey survey) {
        return answerDao.getAnswers(survey)
                .map(ArrayList<Answer>::new);
    }

}
