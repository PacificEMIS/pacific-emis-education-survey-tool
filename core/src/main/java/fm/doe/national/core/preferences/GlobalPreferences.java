package fm.doe.national.core.preferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fm.doe.national.core.preferences.entities.AppRegion;

public interface GlobalPreferences {

    @NonNull
    AppRegion getAppContext();

    void setAppContext(AppRegion appRegion);

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
