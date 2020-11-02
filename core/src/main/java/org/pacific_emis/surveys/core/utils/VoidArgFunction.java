package org.pacific_emis.surveys.core.utils;

/**
 * A functional interface that takes no values
 *
 * @param <T> the output value type
 */
public interface VoidArgFunction<T> {
    /**
     * Apply some calculation.
     * @return t the output value
     */
    T apply();
}
