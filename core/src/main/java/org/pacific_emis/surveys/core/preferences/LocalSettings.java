package org.pacific_emis.surveys.core.preferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omega_r.libs.omegatypes.image.Image;

import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.OperatingMode;
import org.pacific_emis.surveys.core.preferences.entities.SurveyType;

public interface LocalSettings {

    @NonNull
    AppRegion getCurrentAppRegion();

    boolean isCurrentAppRegionSaved();

    void setCurrentAppRegion(AppRegion appRegion);

    @NonNull
    SurveyType getSurveyTypeOrDefault();

    @Nullable
    SurveyType getSurveyType();

    void setSurveyType(SurveyType surveyType);

    Image getLogo();

    @Nullable
    String getLogoPath();

    boolean isLogoSaved();

    void setLogoPath(@Nullable String path);

    String getMasterPassword();

    boolean isMasterPasswordSaved();

    void setMasterPassword(String password);

    String getFactoryPassword();

    Text getAppName();

    boolean isAppNameSaved();

    void setAppName(String name);

    String getContactName();

    boolean isContactNameSaved();

    void setContactName(String name);

    OperatingMode getOperatingMode();

    boolean isOperatingModeSaved();

    void setOperatingMode(OperatingMode mode);

    boolean isExportToExcelEnabled();

    void setExportToExcelEnabled(boolean enabled);

    void setProdCert(String cert);

    @Nullable
    String getProdCert();

    void setEmisApiUrl(String api);

    @Nullable
    String getEmisApiUrl();

    boolean isEmisApiUrlSaved();

    void setEmisUser(String user);

    @Nullable
    String getEmisUser();

    boolean isEmisUserSaved();

    void setEmisPassword(String password);

    @Nullable
    String getEmisPassword();

    boolean isEmisPasswordSaved();

    void setTabletId(String tabletId);

    @Nullable
    String getTabletId();

    boolean isTabletIdSaved();

    void setDrivePageToken(String pageToken);

    String getDrivePageToken();

    boolean isDrivePageTokenSaved();
}
