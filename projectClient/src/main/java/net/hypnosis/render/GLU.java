package net.hypnosis.render;

import org.lwjgl.opengl.GL11;

public class GLU {
    public static void gluPerspective(float fovy, float aspect, float near, float far) {
        float bottom = -near * fovy / 100f;
        float top = -bottom;
        float left = aspect * bottom;
        float right = -left;

        GL11.glFrustum(left, right, bottom, top, near, far);
    }
}
