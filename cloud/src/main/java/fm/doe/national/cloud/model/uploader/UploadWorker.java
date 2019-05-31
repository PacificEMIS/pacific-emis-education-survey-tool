package fm.doe.national.cloud.model.uploader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;

import fm.doe.national.cloud.di.CloudComponentInjector;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.persistence.entity.RoomSchool;
import fm.doe.national.core.data.serialization.serializers.Serializer;
import fm.doe.national.core.di.CoreComponentInjector;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.utils.TextUtil;
import fm.doe.national.cloud.model.CloudRepository;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class UploadWorker extends RxWorker {
    static final String DATA_PASSING_ID = "DATA_PASSING_ID";
    private static final long VALUE_ID_NOT_FOUND = -1;

    private DataSource dataSource;
    private Serializer<Survey> serializer;
    private CloudRepository cloudRepository;

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        CoreComponent coreComponent = CoreComponentInjector.getComponent(context);
        dataSource = coreComponent.getDataSource();
        serializer = coreComponent.getSurveySerializer();
        cloudRepository = CloudComponentInjector.getComponent(context).getCloudRepository();
    }

    @Override
    public Single<Result> createWork() {
        return Single.fromCallable(() -> {
            long passingId = getInputData().getLong(DATA_PASSING_ID, VALUE_ID_NOT_FOUND);
            if (passingId == VALUE_ID_NOT_FOUND) throw new IllegalStateException("passingId == VALUE_ID_NOT_FOUND");
            return passingId;
        })
                .flatMap(dataSource::loadSurvey)
                .flatMapCompletable(survey -> cloudRepository.uploadContent(serializer.serialize(survey), createFilename(survey)))
                .andThen(Single.fromCallable(Result::success));
    }

    @Override
    protected Scheduler getBackgroundScheduler() {
        return Schedulers.io();
    }

    @NonNull
    private String createFilename(Survey survey) {
        return TextUtil.createSurveyFileName(new RoomSchool(survey.getSchoolName(), survey.getSchoolId()), survey.getDate());
    }
}