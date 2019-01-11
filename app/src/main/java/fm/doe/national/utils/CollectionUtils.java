package fm.doe.national.utils;


import java.util.Map;

import androidx.annotation.Nullable;

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
