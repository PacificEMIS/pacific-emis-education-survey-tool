package com.micronesia.data_source;

import com.micronesia.models.survey.Answer;
import com.micronesia.models.survey.Criteria;
import com.micronesia.models.survey.GroupStandard;
import com.micronesia.models.survey.School;
import com.micronesia.models.survey.Standard;
import com.micronesia.models.survey.SubCriteria;
import com.micronesia.models.survey.Survey;

import java.util.List;

import io.reactivex.Single;

public interface AccreditationDataSource {

    Single<School> createSchool(String name);

    Single<List<School>> requestSchools();

    Single<Survey> createSurvey(School school, int year);

    Single<GroupStandard> createGroupStandard();

    Single<Standard> createStandard(String name, GroupStandard group);

    Single<Criteria> createCriteria(String name, Standard standard);

    Single<SubCriteria> createSubCriteria(String name, String question, Criteria criteria);

    Single<Answer> createAnswer(boolean answer, SubCriteria criteria, Survey survey);
}
