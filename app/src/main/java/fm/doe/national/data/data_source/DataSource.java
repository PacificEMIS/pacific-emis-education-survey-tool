package fm.doe.national.data.data_source;


import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.SchoolAccreditationResult;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.Survey;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface DataSource {

    Single<School> createSchool(String name, String id);

    Single<List<School>> requestSchools();

    Single<Answer> createAnswer(boolean answer, SubCriteria criteria, SchoolAccreditationResult result);

    Completable updateAnswer(Answer answer);

    Single<SchoolAccreditation> createNewSchoolAccreditation(int year, School school);

    Single<List<SchoolAccreditationResult>> requestSchoolAccreditationResults();
}
