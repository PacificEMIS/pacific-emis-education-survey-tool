package org.pacific_emis.surveys.offline_sync.domain;

import org.pacific_emis.surveys.offline_sync.data.model.SyncNotification;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class SyncNotifier {

    private PublishSubject<SyncNotification> subject = PublishSubject.create();

    public Observable<SyncNotification> getNotificationsObservable() {
        return subject;
    }

    public void notify(SyncNotification notification) {
        subject.onNext(notification);
    }
}
