package net.minecraft.client.input.mouse;

import net.hypnosis.input.mouse.Mouse;
import net.hypnosis.monitor.Window;
import net.minecraft.client.render.GLAllocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.nio.IntBuffer;

public class MouseHelper {
    private final Window window;
    private final Mouse mouse;

    public double x;
    public double y;

    public double deltaX;
    public double deltaY;

    public MouseHelper(@NotNull final Window window, @NotNull final Mouse mouse) {
        this.window = window;
        this.mouse = mouse;
        this.mouseXYChange();
    }

    public void grabMouseCursor() {
        GLFW.glfwSetInputMode(window.getPointer(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        this.deltaX = 0;
        this.deltaY = 0;

        x = mouse.getX();
        y = mouse.getY();
    }

    public void ungrabMouseCursor() {
        mouse.setCursorPosition(window.getWidth() / 2d, window.getHeight() / 2d);
        GLFW.glfwSetInputMode(window.getPointer(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    }

    public void mouseXYChange() {
        double newX = mouse.getX();
        double newY = mouse.getY();

        this.deltaX = newX - this.x;
        this.deltaY = newY - this.y;

        this.x = newX;
        this.y = newY;
    }
}
