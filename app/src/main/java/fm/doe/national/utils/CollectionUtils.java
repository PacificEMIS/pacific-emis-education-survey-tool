package fm.doe.national.utils;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
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

    public static <T, E> List<T> map(@Nullable List<E> list, ItemMutator<T, E> mutator) {
        List<T> result = new ArrayList<>();

        if (list == null) return result;

        for (E item : list) {
            result.add(mutator.mutate(item));
        }
        return result;
    }

    public interface ItemMutator<T, E> {
        T mutate(E item);
    }

}
