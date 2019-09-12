package net.hypnosis.lwjgl;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

public class LWJGL {

    public static void init(@NotNull final Api api,
                            @NotNull final ContextApi contextApi,
                            @NotNull final Profile profile,
                            int majorVer,
                            int minorVer) {
        if (!GLFW.glfwInit())
            throw new RuntimeException("Failed to init GLFW");

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, api.getConstant());
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_CREATION_API, contextApi.getConstant());
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, profile.getConstant());
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, majorVer);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, minorVer);
    }

    public static void createCapabilities() {
        GL.createCapabilities();
    }
}
