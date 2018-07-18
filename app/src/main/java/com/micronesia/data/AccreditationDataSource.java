package com.micronesia.data;

import com.micronesia.data.models.survey.School;
import com.micronesia.data.models.survey.Survey;

import java.util.List;

import io.reactivex.Single;

public interface AccreditationDataSource {

    Single<School> createSchool(String name);


    Single<List<School>> requestSchools();

    Single<Survey> createSurvey(long schoolId, int year);
}
