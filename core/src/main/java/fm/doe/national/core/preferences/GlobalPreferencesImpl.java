package fm.doe.national.core.preferences;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.OperatingMode;
import fm.doe.national.core.preferences.entities.SurveyType;

public class GlobalPreferencesImpl implements GlobalPreferences {

    private static final String PREF_KEY_APP_REGION = "PREF_KEY_APP_REGION";
    private static final AppRegion DEFAULT_APP_REGION = AppRegion.FCM;
    private static final int NO_APP_CONTEXT_VALUE = -1;

    private static final String PREF_KEY_SURVEY_TYPE = "PREF_KEY_SURVEY_TYPE";
    private static final int NO_SURVEY_TYPE_VALUE = -1;
    private static final SurveyType DEFAULT_SURVEY_TYPE = SurveyType.SCHOOL_ACCREDITATION;

    private static final String PREF_KEY_LOGO_PATH = "PREF_KEY_LOGO_PATH";
    private static final String PREF_KEY_APP_NAME = "PREF_KEY_APP_NAME";
    private static final String PREF_KEY_CONTACT_NAME = "PREF_KEY_CONTACT_NAME";

    private static final String PREF_KEY_OPERATING_MODE = "PREF_KEY_OPERATING_MODE";
    private static final OperatingMode DEFAULT_OPERATING_MODE = OperatingMode.PROD;

    private final SharedPreferences sharedPreferences;

    public GlobalPreferencesImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        AppRegion savedAppRegion = getSavedAppRegion();
        return savedAppRegion != null ? savedAppRegion : DEFAULT_APP_REGION;
    }

    @Override
    public boolean isAppRegionSaved() {
        return getSavedAppRegion() != null;
    }

    @Nullable
    private AppRegion getSavedAppRegion() {
        return AppRegion.createFromValue(sharedPreferences.getInt(PREF_KEY_APP_REGION, NO_APP_CONTEXT_VALUE));
    }

    @Override
    public void setAppRegion(AppRegion appRegion) {
        sharedPreferences.edit().putInt(PREF_KEY_APP_REGION, appRegion.getValue()).apply();
    }

    @Nullable
    @Override
    public String getLogoPath() {
        return sharedPreferences.getString(PREF_KEY_LOGO_PATH, null);
    }

    @Override
    public void setLogoPath(@Nullable String path) {
        sharedPreferences.edit().putString(PREF_KEY_LOGO_PATH, path).apply();
    }

    @Nullable
    @Override
    public SurveyType getSurveyType() {
        return SurveyType.createFromValue(sharedPreferences.getInt(PREF_KEY_SURVEY_TYPE, NO_SURVEY_TYPE_VALUE));
    }

    @NonNull
    @Override
    public SurveyType getSurveyTypeOrDefault() {
        SurveyType savedSurveyType = getSurveyType();
        return savedSurveyType == null ? DEFAULT_SURVEY_TYPE : savedSurveyType;
    }

    @Override
    public void setSurveyType(SurveyType surveyType) {
        sharedPreferences.edit().putInt(PREF_KEY_SURVEY_TYPE, surveyType.getValue()).apply();
    }

    @Override
    public String getMasterPassword() {
        // TODO: not implemented
        return null;
    }

    @Override
    public boolean isMasterPasswordSaved() {
        // TODO: not implemented
        return true;
    }

    @Override
    public void setMasterPassword(String password) {
        // TODO: not implemented
    }

    @Override
    public String getFactoryPassword() {
        // TODO: not implemented
        return null;
    }

    @Override
    public String getAppName() {
        return sharedPreferences.getString(PREF_KEY_APP_NAME, "");
    }

    @Override
    public void setAppName(String name) {
        sharedPreferences.edit().putString(PREF_KEY_APP_NAME, name).apply();
    }

    @Override
    public String getContactName() {
        return sharedPreferences.getString(PREF_KEY_CONTACT_NAME, "");
    }

    @Override
    public void setContactName(String name) {
        sharedPreferences.edit().putString(PREF_KEY_CONTACT_NAME, name).apply();
    }

    @Override
    public OperatingMode getOperatingMode() {
        return OperatingMode.createFromValue(sharedPreferences.getInt(PREF_KEY_OPERATING_MODE, DEFAULT_OPERATING_MODE.getValue()));
    }

    @Override
    public void setOperatingMode(OperatingMode mode) {
        sharedPreferences.edit().putInt(PREF_KEY_OPERATING_MODE, mode.getValue()).apply();
    }
}
