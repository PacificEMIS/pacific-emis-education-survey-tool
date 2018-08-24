package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;

public interface SubCriteria extends Identifiable {
    Criteria getCriteria();

    @NonNull
    String getName();

    Answer getAnswer();

    void setAnswer(Answer answer);
}
