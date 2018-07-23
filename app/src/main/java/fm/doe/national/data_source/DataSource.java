package fm.doe.national.data_source;


import java.util.List;

import fm.doe.national.models.survey.*;
import io.reactivex.Single;

public interface DataSource {

    Single<School> createSchool(String name);

    Single<List<School>> requestSchools();

    Single<Survey> createSurvey(School school, int year);

    Single<GroupStandard> createGroupStandard();

    Single<List<GroupStandard>> requestGroupStandard();

    Single<Standard> createStandard(String name, GroupStandard group);

    Single<Criteria> createCriteria(String name, Standard standard);

    Single<SubCriteria> createSubCriteria(String name, String question, Criteria criteria);

    Single<Answer> createAnswer(boolean answer, SubCriteria criteria, Survey survey);

}
