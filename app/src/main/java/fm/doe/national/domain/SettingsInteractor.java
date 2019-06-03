package fm.doe.national.domain;

import android.content.res.AssetManager;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.util.List;

import fm.doe.national.cloud.model.CloudAccountData;
import fm.doe.national.cloud.model.CloudRepository;
import fm.doe.national.cloud.model.CloudType;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.parsers.Parser;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.preferences.entities.AppRegion;
import io.reactivex.Completable;
import io.reactivex.Single;

public class SettingsInteractor {

    private final CloudRepository cloudRepository;
    private final DataSource localDataRepository;
    private final Parser<Survey> surveyParser;
    private final Parser<List<School>> schoolsParser;
    private final AssetManager assetManager;
    private final GlobalPreferences globalPreferences;

    public SettingsInteractor(CloudRepository cloudRepository,
                              DataSource localDataRepository,
                              Parser<Survey> surveyParser,
                              Parser<List<School>> schoolsParser,
                              AssetManager assetManager,
                              GlobalPreferences globalPreferences) {
        this.cloudRepository = cloudRepository;
        this.localDataRepository = localDataRepository;
        this.surveyParser = surveyParser;
        this.schoolsParser = schoolsParser;
        this.assetManager = assetManager;
        this.globalPreferences = globalPreferences;
    }

    public Completable auth(CloudType type) {
        return cloudRepository.auth(type);
    }

    public Completable importSchools(CloudType type) {
        return cloudRepository.requestContent(type)
                .flatMapCompletable(content -> localDataRepository.rewriteAllSchools(
                        schoolsParser.parse(new ByteArrayInputStream(content.getBytes()))));
    }

    public Completable importSurvey(CloudType type) {
        return cloudRepository.requestContent(type)
                .flatMapCompletable(content -> localDataRepository.rewriteTemplateSurvey(
                        surveyParser.parse(new ByteArrayInputStream(content.getBytes()))));
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
        return Single.fromCallable(() -> surveyParser.parse(assetManager.open(localDataRepository.getSurveyTemplateFileName())))
                .flatMapCompletable(localDataRepository::rewriteTemplateSurvey)
                .andThen(Single.fromCallable(() -> schoolsParser.parse(assetManager.open(localDataRepository.getSchoolsFileName()))))
                .flatMapCompletable(localDataRepository::rewriteAllSchools);
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
