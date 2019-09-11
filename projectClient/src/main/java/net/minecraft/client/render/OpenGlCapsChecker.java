package net.minecraft.client.render;

public class OpenGlCapsChecker {
    private static boolean tryCheckOcclusionCapable = true;

    public boolean checkARBOcclusion() {
        return tryCheckOcclusionCapable && GLContext.getCapabilities().GL_ARB_occlusion_query;
    }
}
