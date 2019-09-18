package net.hypnosis.input.mouse;

@FunctionalInterface
public interface MouseScrollCallback {
    void onScroll(long window, double xOffset, double yOffset);
}
