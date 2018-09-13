package fm.doe.national.data.data_source;


import java.util.List;

import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.Category;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface DataSource {

    Completable addSchools(List<School> schoolList);

    Single<List<School>> requestSchools();

    Completable updateAnswer(long passingId, long subCriteriaId, Answer answer);

    Completable createSchoolAccreditation(LinkedSchoolAccreditation schoolAccreditation);

    Single<LinkedSchoolAccreditation> requestLinkedSchoolAccreditation(long passingId);

    Single<SchoolAccreditationPassing> createNewSchoolAccreditationPassing(int year, School school);

    Single<List<SchoolAccreditationPassing>> requestSchoolAccreditationPassings();

    Single<SchoolAccreditationPassing> requestSchoolAccreditationPassing(long passingId);

    Single<List<Category>> requestCategories(long passingId);

    Single<Standard> requestStandard(long passingId, long standardId);

    Single<List<Standard>> requestStandards(long passingId);

    Single<List<Standard>> requestStandards(long passingId, long categoryId);

    Single<List<Criteria>> requestCriterias(long passingId, long standardId);

    Single<Category> requestCategoryOfStandard(long passingId, Standard standard);
}
