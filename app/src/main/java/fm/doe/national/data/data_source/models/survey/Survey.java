package fm.doe.national.data.data_source.models.survey;

import android.support.annotation.Nullable;

import java.util.Collection;

public interface Survey {
    int getYear();
    School getSchool();

    @Nullable
    Collection<? extends Answer> getAnswers();
}
