package net.hypnosis.util;

public final class Validate {
    public static void validateState(boolean valid, String message) {
        if (valid) {
            throw new IllegalStateException(message);
        }
    }
}
