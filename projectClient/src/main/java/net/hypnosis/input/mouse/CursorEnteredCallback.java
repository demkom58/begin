package net.hypnosis.input.mouse;

@FunctionalInterface
public interface CursorEnteredCallback {
    void onCursorEntered(long window, boolean entered);
}
