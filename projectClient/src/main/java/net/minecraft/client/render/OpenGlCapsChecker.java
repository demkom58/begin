package net.minecraft.client.render;

import org.lwjgl.opengl.GL;

public class OpenGlCapsChecker {
    private static boolean tryCheckOcclusionCapable = true;

    public boolean checkARBOcclusion() {
        return tryCheckOcclusionCapable && GL.getCapabilities().GL_ARB_occlusion_query;
    }
}
