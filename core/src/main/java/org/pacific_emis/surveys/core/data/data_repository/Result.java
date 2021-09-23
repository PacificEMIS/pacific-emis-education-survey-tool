package org.pacific_emis.surveys.core.data.data_repository;

import androidx.annotation.Nullable;

public class Result<T> {

    @Nullable
    private final T data;
    @Nullable
    private final Throwable error;

    public Result(@Nullable T data, @Nullable Throwable error) {
        this.data = data;
        this.error = error;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

}