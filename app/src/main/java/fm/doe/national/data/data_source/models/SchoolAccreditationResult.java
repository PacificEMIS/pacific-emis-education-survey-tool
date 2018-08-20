package fm.doe.national.data.data_source.models;

import java.io.Serializable;
import java.util.Date;

public interface SchoolAccreditationResult extends Serializable {

    int getYear();

    School getSchool();

    SchoolAccreditation getSchoolAccreditation();

    Date getStartDate();

    Date getEndDate();
}
