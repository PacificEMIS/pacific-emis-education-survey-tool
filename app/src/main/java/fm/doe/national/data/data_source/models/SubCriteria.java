package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface SubCriteria extends Identifiable {

    @NonNull
    String getName();

    Answer getAnswer();

    @Nullable
    String getInterviewQuestions();

    @Nullable
    String getHint();

}
