package fm.doe.national.domain;

import java.io.ByteArrayInputStream;
import java.util.List;

import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.parsers.Parser;
import fm.doe.national.data.cloud.CloudAccountData;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.data.cloud.CloudType;
import io.reactivex.Completable;

public class SettingsInteractor {

    private final CloudRepository cloudRepository;
    private final DataSource localDataRepository;
    private final Parser<Survey> surveyParser;
    private final Parser<List<School>> schoolsParser;

    public SettingsInteractor(CloudRepository cloudRepository,
                              DataSource localDataRepository,
                              Parser<Survey> surveyParser,
                              Parser<List<School>> schoolsParser) {
        this.cloudRepository = cloudRepository;
        this.localDataRepository = localDataRepository;
        this.surveyParser = surveyParser;
        this.schoolsParser = schoolsParser;
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
}
