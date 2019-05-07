package fm.doe.national.data.cloud.uploader;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.model.Survey;
import fm.doe.national.data.persistence.entity.PersistenceSchool;
import fm.doe.national.data.serialization.serializers.Serializer;
import fm.doe.national.utils.TextUtil;

public class UploadWorker extends Worker {
    static final String DATA_PASSING_ID = "DATA_PASSING_ID";
    private static final long VALUE_ID_NOT_FOUND = -1;
    private static final String TAG = UploadWorker.class.getName();

    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();
    private final Serializer<Survey> serializer =
            MicronesiaApplication.getAppComponent().getSchoolAccreditationSerizlizer();
    private final CloudRepository cloudRepository = MicronesiaApplication.getAppComponent().getCloudRepository();

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @SuppressWarnings("CheckResult")
    @NonNull
    @Override
    public Result doWork() {
        long passingId = getInputData().getLong(DATA_PASSING_ID, VALUE_ID_NOT_FOUND);
        if (passingId == VALUE_ID_NOT_FOUND) return Result.failure();
        dataSource.loadFullSurvey(passingId)
                .flatMapCompletable(survey -> cloudRepository.uploadContent(serializer.serialize(survey), createFilename(survey)))
                .subscribe(() -> {
                    // nothing
                }, throwable -> Log.e(TAG, "doWork: ", throwable));
        return Result.success();
    }

    @NonNull
    private String createFilename(Survey survey) {
        return TextUtil.createSurveyFileName(new PersistenceSchool(survey.getSchoolName(), survey.getSchoolId()), survey.getDate());
    }
}