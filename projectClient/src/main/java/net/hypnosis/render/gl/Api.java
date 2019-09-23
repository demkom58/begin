package net.hypnosis.render.gl;

import org.lwjgl.glfw.GLFW;

public enum Api {
    NO_API(GLFW.GLFW_NO_API),
    OPENGL(GLFW.GLFW_OPENGL_API),
    OPENGL_ES(GLFW.GLFW_OPENGL_ES_API);

    private final int constant;

    Api(int constant) {
        this.constant = constant;
    }

    public int getConstant() {
        return constant;
    }
}
