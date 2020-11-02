package org.pacific_emis.surveys.core.utils;


import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CollectionUtils {

    @Nullable
    public static <K, V> K getKeyByValue(@NonNull Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static boolean isEmpty(@Nullable Collection collection) {
        return collection == null || collection.isEmpty();
    }

    @NonNull
    public static <T> ArrayList<T> emptyArrayList() {
        return new ArrayList<>();
    }

    @NonNull
    public static <T> ArrayList<T> singletonArrayList(@NonNull T element) {
        ArrayList<T> arrayList = new ArrayList<>();
        arrayList.add(element);
        return arrayList;
    }

    public static <T> void forEach(@NonNull LongSparseArray<T> sparseArray,
                                   @NonNull SparseArrayForEachCallback<T> callback) {
        for (int i = 0; i < sparseArray.size(); i++) {
            final long key = sparseArray.keyAt(i);
            callback.call(key, sparseArray.get(key));
        }
    }

    @NonNull
    public static <T> Iterable<Pair<Long, T>> toIterable(@NonNull LongSparseArray<T> sparseArray) {
        final List<Pair<Long, T>> list = new ArrayList<>();
        for (int i = 0; i < sparseArray.size(); i++) {
            final long key = sparseArray.keyAt(i);
            list.add(Pair.create(key, sparseArray.get(key)));
        }
        return list;
    }

    public interface SparseArrayForEachCallback<T> {
        void call(long id, T item);
    }

}
