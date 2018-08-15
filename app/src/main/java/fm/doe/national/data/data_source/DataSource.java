package fm.doe.national.data.data_source;


import java.util.List;

import fm.doe.national.data.data_source.models.survey.Answer;
import fm.doe.national.data.data_source.models.survey.Criteria;
import fm.doe.national.data.data_source.models.survey.GroupStandard;
import fm.doe.national.data.data_source.models.survey.School;
import fm.doe.national.data.data_source.models.survey.Standard;
import fm.doe.national.data.data_source.models.survey.SubCriteria;
import fm.doe.national.data.data_source.models.survey.Survey;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface DataSource {

    Single<School> createSchool(String name);

    Single<List<School>> requestSchools();

    Single<Survey> createSurvey(School school, int year);

    Single<GroupStandard> createGroupStandard();

    Single<List<GroupStandard>> requestGroupStandard();

    Single<Standard> createStandard(String name, GroupStandard group);

    Single<Criteria> createCriteria(String name, Standard standard);

    Single<SubCriteria> createSubCriteria(String name, Criteria criteria);

    Single<Answer> createAnswer(boolean answer, SubCriteria criteria, Survey survey);

    Completable updateAnswer(Answer answer);
}
