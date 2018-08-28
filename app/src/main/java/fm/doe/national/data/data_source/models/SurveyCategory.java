package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;

public interface SurveyCategory extends Identifiable {

    @NonNull
    String getName();

    int getAnswerCount();

    void setAnswerCount(int count);

}
