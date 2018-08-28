package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;

public interface SubCriteria extends SurveyCategory {
    Criteria getCriteria();

    Answer getAnswer();

    void setAnswer(Answer answer);
}
