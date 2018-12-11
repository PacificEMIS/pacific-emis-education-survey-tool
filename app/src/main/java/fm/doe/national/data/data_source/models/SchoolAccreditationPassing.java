package fm.doe.national.data.data_source.models;

public interface SchoolAccreditationPassing extends SurveyPassing {

    School getSchool();

    SchoolAccreditation getSchoolAccreditation();

}
