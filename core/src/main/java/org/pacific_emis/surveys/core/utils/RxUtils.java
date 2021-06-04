package org.pacific_emis.surveys.core.utils;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class RxUtils {

    public static <T> Completable chain(@NonNull List<Single<T>> singles) {
        if (singles.isEmpty()) {
            return Completable.complete();
        }

        if (singles.size() == 1) {
            return singles.get(0).ignoreElement();
        }

        return singles.get(0).flatMapCompletable(o -> RxUtils.chain(singles.subList(1, singles.size() - 1)));
    }
}
