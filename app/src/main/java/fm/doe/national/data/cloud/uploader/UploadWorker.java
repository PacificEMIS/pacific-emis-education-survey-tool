package fm.doe.national.data.cloud.uploader;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import fm.doe.national.data.serializers.Serializer;
import fm.doe.national.utils.TextUtil;

public class UploadWorker extends Worker {
    static final String DATA_PASSING_ID = "DATA_PASSING_ID";
    private static final long VALUE_ID_NOT_FOUND = -1;
    private static final String TAG = UploadWorker.class.getName();

    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();
    private final Serializer<LinkedSchoolAccreditation> serializer =
            MicronesiaApplication.getAppComponent().getSchoolAccreditationSerizlizer();
    private final CloudRepository cloudRepository = MicronesiaApplication.getAppComponent().getCloudRepository();

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @SuppressWarnings("CheckResult")
    @Override
    public Result doWork() {
        long passingId = getInputData().getLong(DATA_PASSING_ID, VALUE_ID_NOT_FOUND);
        if (passingId == VALUE_ID_NOT_FOUND) return Result.failure();

        dataSource.requestLinkedSchoolAccreditation(passingId)
                .flatMapCompletable(linkedSchoolAccreditation -> dataSource.requestSchoolAccreditationPassing(passingId)
                        .flatMapCompletable(passing -> cloudRepository.uploadContent(
                                serializer.serialize(linkedSchoolAccreditation), createFilename(passing))))
                .subscribe(() -> {
                    // nothing
                }, throwable -> Log.e(TAG, "doWork: ", throwable));
        return Result.success();
    }

    @NonNull
    private String createFilename(SchoolAccreditationPassing passing) {
        return TextUtil.createSurveyFileName(passing.getSchool(), passing.getStartDate());
    }
}