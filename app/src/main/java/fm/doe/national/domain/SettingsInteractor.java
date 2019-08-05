package fm.doe.national.domain;

import android.content.res.AssetManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.util.List;

import fm.doe.national.BuildConfig;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.exceptions.ParseException;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.Parser;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.OperatingMode;
import fm.doe.national.core.preferences.entities.SurveyType;
import fm.doe.national.remote_storage.data.accessor.RemoteStorageAccessor;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class SettingsInteractor {

    private final RemoteStorageAccessor remoteStorageAccessor;
    private final RemoteStorage remoteStorage;
    private final Parser<List<School>> schoolsParser;
    private final AssetManager assetManager;
    private final GlobalPreferences globalPreferences;
    private final SurveyAccessor accessor;

    public SettingsInteractor(RemoteStorageAccessor remoteStorageAccessor,
                              RemoteStorage remoteStorage,
                              Parser<List<School>> schoolsParser,
                              AssetManager assetManager,
                              GlobalPreferences globalPreferences,
                              SurveyAccessor accessor) {
        this.remoteStorageAccessor = remoteStorageAccessor;
        this.remoteStorage = remoteStorage;
        this.accessor = accessor;
        this.schoolsParser = schoolsParser;
        this.assetManager = assetManager;
        this.globalPreferences = globalPreferences;
    }

    public void setOperatingMode(OperatingMode mode) {
        globalPreferences.setOperatingMode(mode);
        remoteStorage.refreshCredentials();
    }

    private DataSource getCurrentDataSource() {
        return accessor.getDataSource(globalPreferences.getSurveyTypeOrDefault());
    }

    public Completable importSchools(String content) {
        return Single.fromCallable(() -> schoolsParser.parse(new ByteArrayInputStream(content.getBytes())))
                .flatMapCompletable(getCurrentDataSource()::rewriteAllSchools);

    }

    public Completable selectExportFolder() {
        return Completable.complete();
    }

    public void showDebugStorage() {
        remoteStorageAccessor.showDebugStorage();
    }

    public Completable loadDataFromAssets() {
        return fetchFsmSchoolsFromAssets()
                .andThen(fetchRmiSchoolsFromAssets())
                .andThen(fetchFsmAccreditationTemplateFromAssets())
                .andThen(fetchRmiAccreditationTemplateFromAssets())
                .andThen(fetchFsmWashTemplateFromAssets())
                .andThen(fetchRmiWashTemplateFromAssets());
    }

    private Completable fetchFsmSchoolsFromAssets() {
        return fetchSchoolsFromAssets(BuildConfig.SCHOOLS_FSM_FILE_NAME);
    }

    private Completable fetchRmiSchoolsFromAssets() {
        return fetchSchoolsFromAssets(BuildConfig.SCHOOLS_RMI_FILE_NAME);
    }

    private Completable fetchSchoolsFromAssets(String fileName) {
        return Single.fromCallable(() -> schoolsParser.parse(assetManager.open(fileName)))
                .flatMapCompletable(getCurrentDataSource()::rewriteAllSchools);
    }

    private Completable fetchFsmAccreditationTemplateFromAssets() {
        return fetchAccreditationTemplateFromAssets(BuildConfig.SURVEY_SA_FSM_FILE_NAME);
    }

    private Completable fetchRmiAccreditationTemplateFromAssets() {
        return fetchAccreditationTemplateFromAssets(BuildConfig.SURVEY_SA_RMI_FILE_NAME);
    }

    private Completable fetchFsmWashTemplateFromAssets() {
        return fetchWashTemplateFromAssets(BuildConfig.SURVEY_WASH_FSM_FILE_NAME);
    }

    private Completable fetchRmiWashTemplateFromAssets() {
        return fetchWashTemplateFromAssets(BuildConfig.SURVEY_WASH_RMI_FILE_NAME);
    }

    private Completable fetchAccreditationTemplateFromAssets(String fileName) {
        return accessor.fetchSurveyTemplateFromAssets(SurveyType.SCHOOL_ACCREDITATION, fileName);
    }

    private Completable fetchWashTemplateFromAssets(String fileName) {
        return accessor.fetchSurveyTemplateFromAssets(SurveyType.WASH, fileName);
    }

    public void setAppRegion(AppRegion region) {
        globalPreferences.setAppRegion(region);
    }

    @Nullable
    public AppRegion getAppRegion() {
        if (globalPreferences.isAppRegionSaved()) {
            return globalPreferences.getAppRegion();
        } else {
            return null;
        }
    }

    public void setMasterPassword(String password) {
        globalPreferences.setMasterPassword(password);
    }

    public boolean isMasterPasswordSaved() {
        return globalPreferences.isMasterPasswordSaved();
    }

    public Completable createFilledSurveyFromCloud() {
        return remoteStorageAccessor.requestContentFromStorage()
                .observeOn(Schedulers.io())
                .flatMapCompletable(accessor::createPartiallySavedSurvey);
    }

    public static class SurveyAccessor {

        private final DataSource accreditationDataSource;
        private final DataSource washDataSource;
        private final Parser<Survey> accreditationSurveyParser;
        private final Parser<Survey> washSurveyParser;
        private final AssetManager assetManager;

        public SurveyAccessor(DataSource accreditationDataSource,
                              DataSource washDataSource,
                              Parser<Survey> accreditationSurveyParser,
                              Parser<Survey> washSurveyParser,
                              AssetManager assetManager) {
            this.accreditationDataSource = accreditationDataSource;
            this.washDataSource = washDataSource;
            this.accreditationSurveyParser = accreditationSurveyParser;
            this.washSurveyParser = washSurveyParser;
            this.assetManager = assetManager;
        }

        public DataSource getDataSource(@NonNull SurveyType surveyType) {
            switch (surveyType) {
                case SCHOOL_ACCREDITATION:
                    return accreditationDataSource;
                case WASH:
                    return washDataSource;
            }
            throw new IllegalStateException();
        }

        public Parser<Survey> getSurveyParser(@NonNull SurveyType surveyType) {
            switch (surveyType) {
                case SCHOOL_ACCREDITATION:
                    return accreditationSurveyParser;
                case WASH:
                    return washSurveyParser;
            }
            throw new IllegalStateException();
        }

        public Completable createPartiallySavedSurvey(String content) throws ParseException {
            Survey survey = tryParseAccreditation(content);

            if (survey != null) {
                return accreditationDataSource.createPartiallySavedSurvey(survey);
            }

            survey = tryParseWash(content);

            if (survey != null) {
                return washDataSource.createPartiallySavedSurvey(survey);
            }

            throw new ParseException();
        }

        @Nullable
        private Survey tryParseAccreditation(String content) {
            return tryParseSurvey(accreditationSurveyParser, content);
        }

        @Nullable
        private Survey tryParseWash(String content) {
            return tryParseSurvey(washSurveyParser, content);
        }

        @Nullable
        private Survey tryParseSurvey(Parser<Survey> parser, String content) {
            try {
                return parser.parse(new ByteArrayInputStream(content.getBytes()));
            } catch (ParseException pe) {
                return null;
            }
        }

        public Completable fetchSurveyTemplateFromAssets(@NonNull SurveyType surveyType, String fileName) {
            return Single.fromCallable(() -> getSurveyParser(surveyType).parse(assetManager.open(fileName)))
                    .flatMapCompletable(getDataSource(surveyType)::rewriteTemplateSurvey);
        }
    }
}
