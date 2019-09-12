package net.minecraft.client.render;

import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class GLAllocation {
    private static List<Integer> displayLists = new ArrayList<>();
    private static List<Integer> textureNames = new ArrayList<>();

    public static synchronized int generateDisplayLists(int s) {
        int genLists = GL11.glGenLists(s);
        displayLists.add(genLists);
        displayLists.add(s);
        return genLists;
    }

    public static synchronized void generateTextureNames(IntBuffer var0) {
        GL11.glGenTextures(var0);

        for (int var1 = var0.position(); var1 < var0.limit(); ++var1) {
            textureNames.add(var0.get(var1));
        }

    }

    public static synchronized void func_28194_b(int var0) {
        int var1 = displayLists.indexOf(var0);
        GL11.glDeleteLists(displayLists.get(var1), displayLists.get(var1 + 1));
        displayLists.remove(var1);
        displayLists.remove(var1);
    }

    public static synchronized void deleteTexturesAndDisplayLists() {
        for (int i = 0; i < displayLists.size(); i += 2) {
            GL11.glDeleteLists(displayLists.get(i), displayLists.get(i + 1));
        }

        IntBuffer buff = createDirectIntBuffer(textureNames.size());
        buff.flip();
        GL11.glDeleteTextures(buff);

        for (int i = 0; i < textureNames.size(); ++i) {
            buff.put(textureNames.get(i));
        }

        buff.flip();
        GL11.glDeleteTextures(buff);
        displayLists.clear();
        textureNames.clear();
    }

    public static synchronized ByteBuffer createDirectByteBuffer(int var0) {
        return ByteBuffer.allocateDirect(var0).order(ByteOrder.nativeOrder());
    }

    public static IntBuffer createDirectIntBuffer(int var0) {
        return createDirectByteBuffer(var0 << 2).asIntBuffer();
    }

    public static FloatBuffer createDirectFloatBuffer(int var0) {
        return createDirectByteBuffer(var0 << 2).asFloatBuffer();
    }
}
