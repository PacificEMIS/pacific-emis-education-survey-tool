package org.pacific_emis.surveys.remote_storage.data.storage;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Tasks;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.SingleSubject;

public abstract class TasksRxWrapper {

    private final Executor executor = Executors.newCachedThreadPool();

    protected <T> Single<T> wrapWithSingleInThreadPool(Callable<T> callable, @NonNull T valueIfError) {
        SingleSubject<T> singleSubject = SingleSubject.create();
        return Completable.fromAction(() -> Tasks.call(executor, callable)
                .addOnSuccessListener(singleSubject::onSuccess)
                .addOnFailureListener(throwable -> {
                    throwable.printStackTrace();
                    singleSubject.onSuccess(valueIfError);
                }))
                .andThen(singleSubject);
    }

    protected Completable wrapWithCompletableInThreadPool(Action action) {
        CompletableSubject subject = CompletableSubject.create();
        return Completable.fromAction(() -> {
            Tasks.call(executor, () -> {
                action.run();
                return null;
            })
                    .addOnSuccessListener(o -> subject.onComplete())
                    .addOnFailureListener(throwable -> {
                        throwable.printStackTrace();
                        subject.onComplete();
                    });
        })
                .andThen(subject);
    }
}
