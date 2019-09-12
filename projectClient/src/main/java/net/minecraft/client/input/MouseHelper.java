package net.minecraft.client.input;

import net.hypnosis.input.mouse.Mouse;
import net.hypnosis.monitor.Window;
import net.minecraft.client.render.GLAllocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.nio.IntBuffer;

public class MouseHelper {
    private final Window window;
    private final Mouse mouse;

    public double deltaX;
    public double deltaY;
//    private Cursor cursor;

    public MouseHelper(@NotNull final Window window, @NotNull final Mouse mouse) {
        this.window = window;
        this.mouse = mouse;

        IntBuffer intBuffer = GLAllocation.createDirectIntBuffer(1);
        intBuffer.put(0);
        intBuffer.flip();
        IntBuffer buffer = GLAllocation.createDirectIntBuffer(1024);

//        try {
//            this.cursor = new Cursor(32, 32, 16, 16, 1, buffer, intBuffer);
//        } catch (LWJGLException e) {
//            e.printStackTrace();
//        }

    }

    public void grabMouseCursor() {
        GLFW.glfwSetInputMode(window.getPointer(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        this.deltaX = 0;
        this.deltaY = 0;
    }

    public void ungrabMouseCursor() {
        mouse.setCursorPosition(window.getWidth() / 2d, window.getHeight() / 2d);
        GLFW.glfwSetInputMode(window.getPointer(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    }

    public void mouseXYChange() {
        this.deltaX = mouse.getDeltaX();
        this.deltaY = mouse.getDeltaY();
    }
}
