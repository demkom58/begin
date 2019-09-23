package net.hypnosis.render.gl;

import org.lwjgl.glfw.GLFW;

public enum Profile {
    ANY(GLFW.GLFW_OPENGL_ANY_PROFILE),
    COMPAT(GLFW.GLFW_OPENGL_COMPAT_PROFILE),
    CORE(GLFW.GLFW_OPENGL_CORE_PROFILE);

    private final int constant;

    Profile(int constant) {
        this.constant = constant;
    }

    public int getConstant() {
        return constant;
    }
}
