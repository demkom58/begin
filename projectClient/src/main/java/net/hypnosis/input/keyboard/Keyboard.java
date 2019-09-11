package net.hypnosis.input.keyboard;

import net.hypnosis.monitor.Window;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class Keyboard {
    private final Window window;

    public Keyboard(@NotNull final Window window) {
        this.window = window;
    }

    public void setCharModsCallback(@Nullable final CharModsCallback charModsCallback) {
        GLFW.glfwSetCharModsCallback(window.getPointer(), charModsCallback == null ? null : charModsCallback::onCharMods);
    }

    public void setKeyCallback(@Nullable final KeyCallback keyCallback) {
        GLFW.glfwSetKeyCallback(window.getPointer(), keyCallback == null ? null : keyCallback::onKey);
    }

    public void setCharCallback(@Nullable final CharCallback charCallback) {
        GLFW.glfwSetCharCallback(window.getPointer(), charCallback == null ? null : charCallback::onChar);
    }

    public boolean isKeyDown(int keyCode) {
        return GLFW.glfwGetKey(window.getPointer(), keyCode) == GLFW.GLFW_PRESS;
    }

}
