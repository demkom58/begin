package net.hypnosis.render.gl;

import org.lwjgl.glfw.GLFW;

public enum ContextApi {
    NATIVE(GLFW.GLFW_NATIVE_CONTEXT_API),
    EGL(GLFW.GLFW_EGL_CONTEXT_API),
    OPENGL_ES(GLFW.GLFW_OPENGL_ES_API);

    private final int constant;

    ContextApi(int constant) {
        this.constant = constant;
    }

    public int getConstant() {
        return constant;
    }
}
