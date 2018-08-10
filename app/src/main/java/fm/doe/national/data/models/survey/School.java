package fm.doe.national.data.models.survey;

import java.util.List;

public interface School {
    String getName();
    List<? extends Survey> getSurveys();
}
