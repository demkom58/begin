package net.hypnosis.input.mouse;

import net.hypnosis.monitor.Window;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

public class Mouse {
    private CursorPositionCallback positionCallback;
    private CursorEnteredCallback enteredCallback;

    private final Window window;

    private double x;
    private double y;

    public double deltaX;
    public double deltaY;

    private boolean entered;

    public Mouse(@NotNull final Window window) {
        this.window = window;
        GLFW.glfwSetCursorEnterCallback(window.getPointer(), this::onEntered);
        GLFW.glfwSetCursorPosCallback(window.getPointer(), this::onMoved);
    }

    private void onMovedWithCallback(long window, double x, double y) {
        this.onMoved(window, x, y);
        this.positionCallback.onCursorPosition(window, x, y);
    }

    private void onMoved(long window, double x, double y) {
        y = this.window.getHeight() - y;

        this.deltaX = x - this.x;
        this.deltaY = y - this.y;

        this.x = x;
        this.y = y;
    }

    private void onEnteredWithCallback(long window, boolean entered) {
        this.onEntered(window, entered);
        this.enteredCallback.onCursorEntered(window, entered);
    }

    private void onEntered(long window, boolean entered) {
        this.entered = entered;
    }

    public void setButtonCallback(@Nullable final MouseButtonCallback buttonCallback) {
        GLFW.glfwSetMouseButtonCallback(window.getPointer(), buttonCallback == null ? null : buttonCallback::onMouseButton);
    }

    public void setScrollCallback(@Nullable final MouseScrollCallback scrollCallback) {
        GLFW.glfwSetScrollCallback(window.getPointer(), scrollCallback == null ? null : scrollCallback::onScroll);
    }

    public void setEnteredCallback(@Nullable final CursorEnteredCallback enteredCallback) {
        this.enteredCallback = enteredCallback;

        if (enteredCallback == null)
            GLFW.glfwSetCursorEnterCallback(window.getPointer(), this::onEntered);
        else
            GLFW.glfwSetCursorEnterCallback(window.getPointer(), this::onEnteredWithCallback);
    }

    public void setPositionCallback(@Nullable final CursorPositionCallback positionCallback) {
        this.positionCallback = positionCallback;

        if (positionCallback == null)
            GLFW.glfwSetCursorPosCallback(window.getPointer(), this::onMoved);
        else
            GLFW.glfwSetCursorPosCallback(window.getPointer(), this::onMovedWithCallback);
    }

    public void setCursorPosition(double x, double y) {
        this.x = x;
        this.y = y;

        GLFW.glfwSetCursorPos(window.getPointer(), x, y);
    }

    public boolean isButtonPressed(int buttonId) {
        return GLFW.glfwGetMouseButton(window.getPointer(), buttonId) == GLFW.GLFW_PRESS;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public boolean isEntered() {
        return entered;
    }
}
