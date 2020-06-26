package org.pacific_emis.surveys.remote_storage.data.uploader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;

import org.pacific_emis.surveys.core.data.data_source.DataSource;
import org.pacific_emis.surveys.data_source_injector.di.DataSourceComponent;
import org.pacific_emis.surveys.data_source_injector.di.DataSourceComponentInjector;
import org.pacific_emis.surveys.remote_storage.data.accessor.RemoteStorageAccessor;
import org.pacific_emis.surveys.remote_storage.data.storage.RemoteStorage;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponent;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponentInjector;
import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class UploadWorker extends RxWorker {
    static final String DATA_PASSING_ID = "DATA_PASSING_ID";
    private static final long VALUE_ID_NOT_FOUND = -1;

    private DataSource dataSource;
    private RemoteStorage remoteStorage;
    private RemoteStorageAccessor remoteStorageAccessor;

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        DataSourceComponent dataSourceComponent = DataSourceComponentInjector.getComponent(context);
        dataSource = dataSourceComponent.getDataSource();
        RemoteStorageComponent remoteStorageComponent = RemoteStorageComponentInjector.getComponent(context);
        remoteStorage = remoteStorageComponent.getRemoteStorage();
        remoteStorageAccessor = remoteStorageComponent.getRemoteStorageAccessor();
    }

    @Override
    public Single<Result> createWork() {
        final long surveyId = getInputData().getLong(DATA_PASSING_ID, VALUE_ID_NOT_FOUND);
        return Single.fromCallable(() -> {
            if (surveyId == VALUE_ID_NOT_FOUND) throw new IllegalStateException("surveyId == VALUE_ID_NOT_FOUND");
            return surveyId;
        })
                .flatMap(dataSource::loadSurvey)
                .flatMapCompletable(survey -> remoteStorage.upload(survey))
                .onErrorResumeNext(throwable -> {
                    throwable.printStackTrace();
                    remoteStorageAccessor.scheduleUploading(surveyId);
                    return Completable.complete();
                })
                .andThen(Single.just(Result.success()));
    }

    @Override
    protected Scheduler getBackgroundScheduler() {
        return Schedulers.io();
    }
}