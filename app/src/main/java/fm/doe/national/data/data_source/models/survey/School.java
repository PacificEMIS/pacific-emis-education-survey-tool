package fm.doe.national.data.data_source.models.survey;

import java.util.Collection;

public interface School {
    String getName();
    Collection<? extends Survey> getSurveys();
}
