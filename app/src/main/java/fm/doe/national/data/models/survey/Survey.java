package fm.doe.national.data.models.survey;

import android.support.annotation.Nullable;

import java.util.List;

public interface Survey {
    int getYear();
    School getSchool();

    @Nullable
    List<? extends Answer> getAnswers();
}
