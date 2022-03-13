package net.hypnosis.util;

import com.google.common.base.Supplier;

import java.util.Objects;

public final class Result<T> {
    private final Object content;
    private final boolean error;

    private Result(Object content, boolean error) {
        this.content = content;
        this.error = error;
    }

    public boolean isError() {
        return error;
    }

    @SuppressWarnings("unchecked")
    public T unwrap() {
        if (error) {
            throw new IllegalStateException((String) content);
        }

        return (T) content;
    }

    public String message() {
        if (error) {
            return (String) content;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public T orElse(Supplier<T> supplier) {
        if (error) {
            return supplier.get();
        }

        return (T) content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?> result = (Result<?>) o;
        return error == result.error && Objects.equals(content, result.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, error);
    }

    @Override
    public String toString() {
        return "Result{" +
                "content=" + content +
                ", error=" + error +
                '}';
    }

    public static <T> Result<T> success(T content) {
        return new Result<>(content, false);
    }

    public static <T> Result<T> error(String error) {
        return new Result<>(error, true);
    }
}
