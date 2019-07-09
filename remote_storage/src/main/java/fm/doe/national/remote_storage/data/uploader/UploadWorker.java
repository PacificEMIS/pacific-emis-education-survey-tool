package fm.doe.national.remote_storage.data.uploader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;

import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.SurveySerializer;
import fm.doe.national.data_source_injector.di.DataSourceComponent;
import fm.doe.national.data_source_injector.di.DataSourceComponentInjector;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import fm.doe.national.remote_storage.di.RemoteStorageComponentInjector;
import fm.doe.national.remote_storage.utils.TextUtil;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class UploadWorker extends RxWorker {
    static final String DATA_PASSING_ID = "DATA_PASSING_ID";
    private static final long VALUE_ID_NOT_FOUND = -1;

    private DataSource dataSource;
    private SurveySerializer surveySerializer;
    private RemoteStorage remoteStorage;

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        DataSourceComponent dataSourceComponent = DataSourceComponentInjector.getComponent(context);
        dataSource = dataSourceComponent.getDataSource();
        surveySerializer = dataSourceComponent.getSurveySerializer();
        remoteStorage = RemoteStorageComponentInjector.getComponent(context).getRemoteStorage();
    }

    @Override
    public Single<Result> createWork() {
        return Single.fromCallable(() -> {
            long passingId = getInputData().getLong(DATA_PASSING_ID, VALUE_ID_NOT_FOUND);
            if (passingId == VALUE_ID_NOT_FOUND) throw new IllegalStateException("passingId == VALUE_ID_NOT_FOUND");
            return passingId;
        })
                .flatMap(dataSource::loadSurvey)
                .flatMapCompletable(survey -> remoteStorage.uploadContent(
                        surveySerializer.serialize(survey),
                        createFilePath(survey),
                        survey.getAppRegion())
                )
                .andThen(Single.fromCallable(Result::success));
    }

    @Override
    protected Scheduler getBackgroundScheduler() {
        return Schedulers.io();
    }

    @NonNull
    private String createFilePath(Survey survey) {
        return TextUtil.createSurveyFileName(survey);
    }
}