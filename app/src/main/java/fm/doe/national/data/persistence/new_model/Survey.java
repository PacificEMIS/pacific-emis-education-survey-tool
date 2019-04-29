package fm.doe.national.data.persistence.new_model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

public interface Survey extends IdentifiedObject {

    int getVersion();

    @NonNull
    SurveyType getSurveyType();

    @NonNull
    Date getDate();

    @Nullable
    String getSchoolName();

    @Nullable
    String getSchoolId();

    @NonNull
    List<Category> getCategories();

}
