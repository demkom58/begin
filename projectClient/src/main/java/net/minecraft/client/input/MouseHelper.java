package net.minecraft.client.input;

import net.minecraft.client.render.GLAllocation;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.nio.IntBuffer;

public class MouseHelper {
    public int deltaX;
    public int deltaY;
    private Cursor cursor;

    public MouseHelper() {
        IntBuffer intBuffer = GLAllocation.createDirectIntBuffer(1);
        intBuffer.put(0);
        intBuffer.flip();
        IntBuffer buffer = GLAllocation.createDirectIntBuffer(1024);

        try {
            this.cursor = new Cursor(32, 32, 16, 16, 1, buffer, intBuffer);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

    }

    public void grabMouseCursor() {
        Mouse.setGrabbed(true);
        this.deltaX = 0;
        this.deltaY = 0;
    }

    public void ungrabMouseCursor() {
        Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
        Mouse.setGrabbed(false);
    }

    public void mouseXYChange() {
        this.deltaX = Mouse.getDX();
        this.deltaY = Mouse.getDY();
    }
}
