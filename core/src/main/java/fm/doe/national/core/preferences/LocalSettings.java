package fm.doe.national.core.preferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Image;
import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.OperatingMode;
import fm.doe.national.core.preferences.entities.SurveyType;

public interface LocalSettings {

    @NonNull
    AppRegion getAppRegion();

    boolean isAppRegionSaved();

    void setAppRegion(AppRegion appRegion);

    @NonNull
    SurveyType getSurveyTypeOrDefault();

    @Nullable
    SurveyType getSurveyType();

    void setSurveyType(SurveyType surveyType);

    Image getLogo();

    void setLogoPath(@Nullable String path);

    String getMasterPassword();

    boolean isMasterPasswordSaved();

    void setMasterPassword(String password);

    String getFactoryPassword();

    Text getAppName();

    void setAppName(String name);

    String getContactName();

    void setContactName(String name);

    OperatingMode getOperatingMode();

    void setOperatingMode(OperatingMode mode);

    String getSpreadsheetId();

    boolean isExportToExcelEnabled();

    void setExportToExcelEnabled(boolean enabled);

    void setProdCert(String cert);

    @Nullable
    String getProdCert();
}
