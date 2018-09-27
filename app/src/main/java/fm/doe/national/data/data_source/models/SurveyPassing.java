package fm.doe.national.data.data_source.models;

import java.util.Date;

public interface SurveyPassing extends Identifiable {
    Date getStartDate();
    Date getEndDate();
}
