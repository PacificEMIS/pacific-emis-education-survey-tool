package fm.doe.national.core.preferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fm.doe.national.core.preferences.entities.AccreditationType;
import fm.doe.national.core.preferences.entities.AppRegion;

public interface GlobalPreferences {

    @NonNull
    AppRegion getAppRegion();

    void setAppRegion(AppRegion appRegion);

    @NonNull
    AccreditationType getAccreditationType();

    void setAccreditationType(AccreditationType accreditationType);

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
