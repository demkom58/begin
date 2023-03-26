package net.hypnosis.util;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;

public class NativeImage implements AutoCloseable {
    private final int width;
    private final int height;
    private final int channels;
    private final ByteBuffer pixels;

    public NativeImage(int width, int height, int channels, ByteBuffer pixels) {
        this.width = width;
        this.height = height;
        this.channels = channels;
        this.pixels = pixels;
    }

    @Override
    public void close() throws Exception {
        stbi_image_free(this.pixels);
    }

    public ByteBuffer getPixels() {
        return pixels;
    }

    public int getChannels() {
        return channels;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public static NativeImage load(String path) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            boolean result = stbi_info(path, width, height, channels);
            if (!result) {
                throw new RuntimeException("Failed to load info about image " + path);
            }

            ByteBuffer pixels = stbi_load(path, width, height, channels, 4);
            if (pixels == null) {
                throw new RuntimeException("Failed to load image " + path + ": " + stbi_failure_reason());
            }

            return new NativeImage(width.get(), height.get(), channels.get(), pixels);
        }
    }

    public static NativeImage loadClasspath(String path) {
        ByteBuffer tempo = null;
        try (MemoryStack stack = MemoryStack.stackPush();
             InputStream imgStream = ClassLoader.getSystemClassLoader().getResourceAsStream(path)) {

            if (imgStream == null) {
                throw new RuntimeException("Failed to load image " + path + ": image not found");
            }

            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            byte[] bytes = imgStream.readAllBytes();
            tempo = MemoryUtil.memAlloc(bytes.length);
            tempo.put(bytes);
            tempo.flip();

            boolean result = stbi_info_from_memory(tempo, width, height, channels);
            if (!result) {
                throw new RuntimeException("Failed to load info about image " + path);
            }

            ByteBuffer pixels = stbi_load_from_memory(tempo, width, height, channels, 4);
            if (pixels == null) {
                throw new RuntimeException("Failed to load image " + path + ": " + stbi_failure_reason());
            }

            return new NativeImage(width.get(), height.get(), channels.get(), pixels);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (tempo != null) {
                MemoryUtil.memFree(tempo);
            }
        }
    }
}
