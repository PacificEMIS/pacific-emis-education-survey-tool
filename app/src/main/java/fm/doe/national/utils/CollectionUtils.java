package fm.doe.national.utils;

import android.support.annotation.Nullable;

import java.util.Map;

public class CollectionUtils {

    @Nullable
    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
