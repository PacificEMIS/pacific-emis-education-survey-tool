package fm.doe.national.domain;

import java.io.ByteArrayInputStream;
import java.util.List;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.CloudAccountData;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.data.cloud.CloudType;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.parsers.Parser;
import io.reactivex.Completable;

public class SettingsInteractor {
    private final CloudRepository cloudRepository = MicronesiaApplication.getAppComponent().getCloudRepository();
    private final DataSource localDataRepository = MicronesiaApplication.getAppComponent().getDataSource();
    private final Parser<SchoolAccreditation> surveyParser = MicronesiaApplication.getAppComponent().getSurveyParser();
    private final Parser<List<School>> schoolsParser = MicronesiaApplication.getAppComponent().getSchoolsParser();

    public Completable auth(CloudType type) {
        return cloudRepository.auth(type);
    }

    public Completable importSchools(CloudType type) {
        return cloudRepository.requestContent(type)
                .flatMapCompletable(content -> localDataRepository.addSchools(
                        schoolsParser.parse(new ByteArrayInputStream(content.getBytes()))));
    }

    public Completable importSurvey(CloudType type) {
        return cloudRepository.requestContent(type)
                .flatMapCompletable(content -> localDataRepository.createSchoolAccreditation(
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

    public List<CloudAccountData> getNotConnectedAccounts() {
        return cloudRepository.getUnusedAccounts();
    }
}
