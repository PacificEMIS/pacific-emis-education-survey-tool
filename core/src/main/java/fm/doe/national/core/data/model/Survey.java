package fm.doe.national.core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

import fm.doe.national.core.preferences.entities.AppRegion;

public interface Survey extends Progressable, IdentifiedObject {

    int getVersion();

    @NonNull
    SurveyType getSurveyType();

    @Nullable
    Date getDate();

    @Nullable
    String getSchoolName();

    @Nullable
    String getSchoolId();

    @NonNull
    AppRegion getAppRegion();

    @Nullable
    List<? extends Category> getCategories();

}
