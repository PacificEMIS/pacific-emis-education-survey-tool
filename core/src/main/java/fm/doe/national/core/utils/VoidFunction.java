package fm.doe.national.core.utils;

import io.reactivex.annotations.NonNull;

/**
 * A functional interface that takes a value
 *
 * @param <T> the input value type
 */
public interface VoidFunction<T> {
    /**
     * Apply some calculation to the input value.
     * @param t the input value
     */
    void apply(@NonNull T t);
}
