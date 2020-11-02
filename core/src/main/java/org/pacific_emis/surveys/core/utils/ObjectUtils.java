package org.pacific_emis.surveys.core.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ObjectUtils {

    public static <T> T orElse(@Nullable T object, @NonNull T resultIfNull) {
        return object == null ? resultIfNull : object;
    }
}
