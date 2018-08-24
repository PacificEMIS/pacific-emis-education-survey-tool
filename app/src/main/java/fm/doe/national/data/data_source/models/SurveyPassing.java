package fm.doe.national.data.data_source.models;

import java.io.Serializable;
import java.util.Date;

public interface SurveyPassing extends Serializable {
    int getYear();
    Date getStartDate();
    Date getEndDate();
}
