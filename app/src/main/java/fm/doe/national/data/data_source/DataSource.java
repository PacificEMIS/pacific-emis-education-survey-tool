package fm.doe.national.data.data_source;


import java.util.List;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.SurveyPassing;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface DataSource {

    Single<School> createSchool(String name, String id);

    Completable addSchools(List<School> schoolList);

    Single<List<School>> requestSchools();

    Single<Answer> createAnswer(Answer.State state, SubCriteria criteria, SurveyPassing result);

    Completable updateAnswer(SubCriteria subCriteria);

    Completable createSchoolAccreditation(SchoolAccreditation schoolAccreditation);

    Single<SchoolAccreditation> createNewSchoolAccreditationPassing(int year, School school);

    Single<List<SchoolAccreditationPassing>> requestSchoolAccreditationPassings();
}
