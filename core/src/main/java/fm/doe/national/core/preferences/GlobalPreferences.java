package fm.doe.national.core.preferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fm.doe.national.core.data.model.CloudType;
import fm.doe.national.core.preferences.entities.SurveyType;
import fm.doe.national.core.preferences.entities.AppRegion;

public interface GlobalPreferences {

    @NonNull
    AppRegion getAppRegion();

    void setAppRegion(AppRegion appRegion);

    @NonNull
    SurveyType getSurveyType();

    void setSurveyType(SurveyType surveyType);

    @NonNull
    CloudType getCloudType();

    void setCloudType(CloudType cloudType);

    @Nullable
    String getLogoPath();

    void setLogoPath(@Nullable String path);

    boolean isFirstLaunch();

    // TODO: will be implemented later
    // String getMasterPassword();
    //
    // void setMasterPassword();
    //
    // String getFactoryPassword();

}
