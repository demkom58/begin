package net.hypnosis.input.mouse;

@FunctionalInterface
public interface MouseButtonCallback {
    void onMouseButton(long window, int button, int action, int mods);
}
