package fm.doe.national.core.utils;

import androidx.annotation.Nullable;

public class RxNullableObject<T> {
    @Nullable
    public T object;

    public static <T> RxNullableObject<T> wrap(@Nullable T object) {
        return new RxNullableObject<>(object);
    }

    private RxNullableObject(@Nullable T object) {
        this.object = object;
    }

    public boolean isNull() {
        return object == null;
    }
}
