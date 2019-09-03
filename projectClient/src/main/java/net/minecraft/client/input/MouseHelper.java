package net.minecraft.client.input;

import net.minecraft.client.render.GLAllocation;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.nio.IntBuffer;

public class MouseHelper {
    public int deltaX;
    public int deltaY;
    private Component component;
    private Cursor cursor;
    private int field_1115_e = 10;

    public MouseHelper(Component component) {
        this.component = component;
        IntBuffer intBuffer = GLAllocation.createDirectIntBuffer(1);
        intBuffer.put(0);
        intBuffer.flip();
        IntBuffer var3 = GLAllocation.createDirectIntBuffer(1024);

        try {
            this.cursor = new Cursor(32, 32, 16, 16, 1, var3, intBuffer);
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
        Mouse.setCursorPosition(this.component.getWidth() / 2, this.component.getHeight() / 2);
        Mouse.setGrabbed(false);
    }

    public void mouseXYChange() {
        this.deltaX = Mouse.getDX();
        this.deltaY = Mouse.getDY();
    }
}
