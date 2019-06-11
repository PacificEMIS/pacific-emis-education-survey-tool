package fm.doe.national.domain;

import android.content.res.AssetManager;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.util.List;

import fm.doe.national.BuildConfig;
import fm.doe.national.cloud.model.CloudAccountData;
import fm.doe.national.cloud.model.CloudRepository;
import fm.doe.national.cloud.model.CloudType;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.exceptions.ParseException;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.Parser;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.preferences.entities.AppRegion;
import io.reactivex.Completable;
import io.reactivex.Single;

public class SettingsInteractor {

    private final CloudRepository cloudRepository;
    private final DataSource washDataSource;
    private final DataSource accreditationDataSource;
    private final Parser<List<School>> schoolsParser;
    private final Parser<Survey> accreditationSurveyParser;
    private final Parser<Survey> washSurveyParser;
    private final AssetManager assetManager;
    private final GlobalPreferences globalPreferences;

    public SettingsInteractor(CloudRepository cloudRepository,
                              DataSource accreditationDataSource,
                              DataSource washDataSource,
                              Parser<Survey> accreditationSurveyParser,
                              Parser<Survey> washSurveyParser,
                              Parser<List<School>> schoolsParser,
                              AssetManager assetManager,
                              GlobalPreferences globalPreferences) {
        this.cloudRepository = cloudRepository;
        this.washDataSource = washDataSource;
        this.accreditationDataSource = accreditationDataSource;
        this.accreditationSurveyParser = accreditationSurveyParser;
        this.washSurveyParser = washSurveyParser;
        this.schoolsParser = schoolsParser;
        this.assetManager = assetManager;
        this.globalPreferences = globalPreferences;
    }

    public Completable auth(CloudType type) {
        return cloudRepository.auth(type);
    }

    private DataSource getCurrentDataSource() {
        switch (globalPreferences.getSurveyTypeOrDefault()) {
            case SCHOOL_ACCREDITATION:
                return accreditationDataSource;
            case WASH:
                return washDataSource;
        }
        throw new IllegalStateException();
    }

    public Completable importSchools(CloudType type) {
        return cloudRepository.requestContent(type)
                .flatMapCompletable(content -> getCurrentDataSource().rewriteAllSchools(
                        schoolsParser.parse(new ByteArrayInputStream(content.getBytes()))));
    }

    public Completable importSurvey(CloudType type) {
        return cloudRepository.requestContent(type)
                .flatMapCompletable(content -> {
                    Survey survey = tryParseAccreditation(content);

                    if (survey != null) {
                        return accreditationDataSource.rewriteTemplateSurvey(survey);
                    }

                    survey = tryParseWash(content);

                    if (survey != null) {
                        return washDataSource.rewriteTemplateSurvey(survey);
                    }

                    throw new ParseException();
                });
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

    public Completable selectExportFolder(CloudType type) {
        return cloudRepository.chooseExportFolder(type);
    }

    public void setDefaultCloudForExport(CloudType type) {
        cloudRepository.setPrimary(type);
    }

    public List<CloudAccountData> getConnectedAccounts() {
        return cloudRepository.getUsedAccounts();
    }

    public Completable loadDataFromAssets() {
        return fetchFcmSchoolsFromAssets()
                .andThen(fetchRmiSchoolsFromAssets())
                .andThen(fetchFcmAccreditationTemplateFromAssets())
                .andThen(fetchRmiAccreditationTemplateFromAssets())
                .andThen(fetchFcmWashTemplateFromAssets())
                .andThen(fetchRmiWashTemplateFromAssets());
    }

    private Completable fetchFcmSchoolsFromAssets() {
        return fetchSchoolsFromAssets(BuildConfig.SCHOOLS_FCM_FILE_NAME);
    }

    private Completable fetchRmiSchoolsFromAssets() {
        return fetchSchoolsFromAssets(BuildConfig.SCHOOLS_RMI_FILE_NAME);
    }

    private Completable fetchSchoolsFromAssets(String fileName) {
        return Single.fromCallable(() -> schoolsParser.parse(assetManager.open(fileName)))
                .flatMapCompletable(getCurrentDataSource()::rewriteAllSchools);
    }

    private Completable fetchFcmAccreditationTemplateFromAssets() {
        return fetchAccreditationTemplateFromAssets(BuildConfig.SURVEY_FCM_FILE_NAME);
    }

    private Completable fetchRmiAccreditationTemplateFromAssets() {
        return fetchAccreditationTemplateFromAssets(BuildConfig.SURVEY_RMI_FILE_NAME);
    }

    private Completable fetchFcmWashTemplateFromAssets() {
        return fetchWashTemplateFromAssets(BuildConfig.SURVEY_WASH_FCM_FILE_NAME);
    }

    private Completable fetchRmiWashTemplateFromAssets() {
        return fetchWashTemplateFromAssets(BuildConfig.SURVEY_WASH_RMI_FILE_NAME);
    }

    private Completable fetchAccreditationTemplateFromAssets(String fileName) {
        return fetchSurveyTemplateFromAssets(accreditationSurveyParser, accreditationDataSource, fileName);
    }

    private Completable fetchWashTemplateFromAssets(String fileName) {
        return fetchSurveyTemplateFromAssets(washSurveyParser, washDataSource, fileName);
    }

    private Completable fetchSurveyTemplateFromAssets(Parser<Survey> parser, DataSource dataSource, String fileName) {
        return Single.fromCallable(() -> parser.parse(assetManager.open(fileName)))
                .flatMapCompletable(dataSource::rewriteTemplateSurvey);
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
}
