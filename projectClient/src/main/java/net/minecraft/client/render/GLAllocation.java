package net.minecraft.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class GLAllocation {
    private static List<Integer> displayLists = new ArrayList<>();
    private static List<Integer> textureNames = new ArrayList<>();

    public static synchronized int generateDisplayLists(int searchedMemLen) {
        int genLists = GL11.glGenLists(searchedMemLen);

        if (genLists == MemoryUtil.NULL)
            throw new RuntimeException("Failed to allocate lists");

        displayLists.add(genLists);
        displayLists.add(searchedMemLen);

        return genLists;
    }

    public static synchronized void generateTextureNames(IntBuffer buff) {
        GL11.glGenTextures(buff);

        for (int i = buff.position(); i < buff.limit(); ++i) {
            textureNames.add(buff.get(i));
        }

    }

    public static synchronized void func_28194_b(int elem) {
        int index = displayLists.indexOf(elem);
        GL11.glDeleteLists(displayLists.get(index), displayLists.get(index + 1));
        displayLists.remove(index);
        displayLists.remove(index);
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
