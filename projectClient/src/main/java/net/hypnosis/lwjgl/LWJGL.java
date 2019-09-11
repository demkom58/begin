package net.hypnosis.lwjgl;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class LWJGL {
    public static void init(@NotNull final Api api,
                            @NotNull final ContextApi contextApi,
                            @NotNull final Profile profile,
                            int majorContextVersion,
                            int minorContextVersion) {
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, api.getConstant());
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_CREATION_API, contextApi.getConstant());
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, profile.getConstant());
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, majorContextVersion);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, minorContextVersion);
    }
}
