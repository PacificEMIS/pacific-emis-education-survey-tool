package fm.doe.national.core.preferences;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.SurveyType;

public class GlobalPreferencesImpl implements GlobalPreferences {

    private static final String PREF_KEY_APP_REGION = "PREF_KEY_APP_REGION";
    private static final AppRegion DEFAULT_APP_REGION = AppRegion.FCM;
    private static final int NO_APP_CONTEXT_VALUE = -1;

    private static final String PREF_KEY_ACCREDITATION_TYPE = "PREF_KEY_ACCREDITATION_TYPE";
    private static final SurveyType DEFAULT_ACCREDITATION_TYPE = SurveyType.ACCREDITATION;
    private static final int NO_ACCREDITATION_TYPE_VALUE = -1;

    private static final String PREF_KEY_LOGO_PATH = "PREF_KEY_LOGO_PATH";

    private final SharedPreferences sharedPreferences;

    public GlobalPreferencesImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        AppRegion savedAppRegion = getSavedAppContext();
        return savedAppRegion != null ? savedAppRegion : DEFAULT_APP_REGION;
    }

    @Nullable
    private AppRegion getSavedAppContext() {
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

    @Override
    public boolean isFirstLaunch() {
        return getSavedAppContext() == null;
    }

    @NonNull
    @Override
    public SurveyType getSurveyType() {
        SurveyType savedSurveyType = getSavedAccreditationType();
        return savedSurveyType != null ? savedSurveyType : DEFAULT_ACCREDITATION_TYPE;
    }

    @Override
    public void setSurveyType(SurveyType surveyType) {
        sharedPreferences.edit().putInt(PREF_KEY_ACCREDITATION_TYPE, surveyType.getValue()).apply();
    }

    @Nullable
    private SurveyType getSavedAccreditationType() {
        return SurveyType.createFromValue(sharedPreferences.getInt(PREF_KEY_ACCREDITATION_TYPE, NO_ACCREDITATION_TYPE_VALUE));
    }

}
