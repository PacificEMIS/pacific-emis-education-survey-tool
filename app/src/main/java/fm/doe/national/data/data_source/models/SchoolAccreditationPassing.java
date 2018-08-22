package fm.doe.national.data.data_source.models;

import java.util.Date;

public interface SchoolAccreditationPassing extends SurveyPassing {

    School getSchool();

    SchoolAccreditation getSchoolAccreditation();

}
