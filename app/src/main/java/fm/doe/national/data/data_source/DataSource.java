package fm.doe.national.data.data_source;


import java.util.List;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.data.data_source.models.SurveyPassing;
import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface DataSource {

    Single<School> createSchool(String name, String id);

    Completable addSchools(List<School> schoolList);

    Single<List<School>> requestSchools();

    Single<Answer> createAnswer(Answer.State state, SubCriteria criteria, SurveyPassing result);

    Completable updateAnswer(long passingId, long subcriteriaId, Answer.State state);

    Completable createSchoolAccreditation(LinkedSchoolAccreditation schoolAccreditation);

    Single<LinkedSchoolAccreditation> requestLinkedSchoolAccreditation();

    Single<SchoolAccreditationPassing> createNewSchoolAccreditationPassing(int year, School school);

    Single<List<SchoolAccreditationPassing>> requestSchoolAccreditationPassings();

    Single<List<GroupStandard>> requestGroupStandards(long passingId);

    Single<List<Standard>> requestStandards(long passingId, long groupStandardId);

    Single<List<Criteria>> requestCriterias(long passingId, long standardId);
}
