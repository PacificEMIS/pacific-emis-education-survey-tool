package org.pacific_emis.surveys.core.utils;

import androidx.annotation.NonNull;

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
