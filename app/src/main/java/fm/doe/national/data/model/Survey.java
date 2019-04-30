package fm.doe.national.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

public interface Survey extends IdentifiedObject {

    int getVersion();

    @NonNull
    SurveyType getSurveyType();

    @Nullable
    Date getDate();

    @Nullable
    String getSchoolName();

    @Nullable
    String getSchoolId();

    @Nullable
    List<? extends Category> getCategories();

}
