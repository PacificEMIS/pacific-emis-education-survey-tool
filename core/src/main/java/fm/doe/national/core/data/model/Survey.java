package fm.doe.national.core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.SurveyType;

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

}
