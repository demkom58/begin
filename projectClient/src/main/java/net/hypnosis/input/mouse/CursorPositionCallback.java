package net.hypnosis.input.mouse;

@FunctionalInterface
public interface CursorPositionCallback {
    void onCursorPosition(long window, double x, double y);;
}
