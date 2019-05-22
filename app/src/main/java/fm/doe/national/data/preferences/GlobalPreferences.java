package fm.doe.national.data.preferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fm.doe.national.data.preferences.entities.AppContext;

public interface GlobalPreferences {

    @NonNull
    AppContext getAppContext();

    void setAppContext(AppContext appContext);

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
