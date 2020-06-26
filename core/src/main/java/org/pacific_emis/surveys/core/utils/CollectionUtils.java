package org.pacific_emis.surveys.core.utils;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
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

}
