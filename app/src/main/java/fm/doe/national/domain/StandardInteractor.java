package fm.doe.national.domain;

import java.util.List;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.uploader.CloudUploader;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.Standard;
import io.reactivex.Completable;
import io.reactivex.Single;

public class StandardInteractor {
    private final CloudUploader cloudUploader = MicronesiaApplication.getAppComponent().getCloudUploader();
    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();


    public Completable answerGiven(long passingId, long subCriteriaId, Answer.State state) {
        return dataSource.updateAnswer(passingId, subCriteriaId, state)
                .andThen(Completable.fromAction(() -> cloudUploader.scheduleUploading(passingId)));
    }

    public Single<List<Criteria>> requestCriterias(long passingId, long standardId) {
        return dataSource.requestCriterias(passingId, standardId);
    }

    public Single<List<Standard>> requestStandards(long passingId) {
        return dataSource.requestStandards(passingId);
    }

    public Single<Standard> requestStandard(long passingId, long standardId) {
        return dataSource.requestStandard(passingId, standardId);
    }

    public Single<SchoolAccreditationPassing> requestSchoolAccreditationPassing(long passingId) {
        return dataSource.requestSchoolAccreditationPassing(passingId);
    }
}
