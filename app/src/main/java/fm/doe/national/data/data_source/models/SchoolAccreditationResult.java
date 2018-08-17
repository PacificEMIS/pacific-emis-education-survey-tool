package fm.doe.national.data.data_source.models;

import java.util.Date;

public interface SchoolAccreditationResult {

    int getYear();

    School getSchool();

    SchoolAccreditation getSchoolAccreditation();

    Date getStartDate();

    Date getEndDate();
}
