package net.hypnosis.audio;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.openal.AL10;

import java.util.Objects;

public class Sound {
    private final int pointer;

    private final String name;

    private final int channels;
    private final int sampleRate;
    private final int format;

    private final int bufferPointer;

    public Sound(int pointer,
                 @NotNull final String name,
                 int channels, int sampleRate, int format, int bufferPointer) {
        this.pointer = pointer;

        this.name = name;

        this.channels = channels;
        this.sampleRate = sampleRate;
        this.format = format;

        this.bufferPointer = bufferPointer;
    }

    public int getPointer() {
        return pointer;
    }

    public @NotNull String getName() {
        return name;
    }

    public int getChannels() {
        return channels;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getFormat() {
        return format;
    }

    public int getBufferPointer() {
        return bufferPointer;
    }

    public void dispose() {
        AL10.alDeleteBuffers(bufferPointer);
    }

    @Override
    public String toString() {
        return "Sound{" +
                "pointer=" + pointer +
                ", channels=" + channels +
                ", sampleRate=" + sampleRate +
                ", format=" + format +
                ", bufferPointer=" + bufferPointer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sound sound = (Sound) o;
        return pointer == sound.pointer &&
                channels == sound.channels &&
                sampleRate == sound.sampleRate &&
                format == sound.format &&
                bufferPointer == sound.bufferPointer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointer, channels, sampleRate, format, bufferPointer);
    }

}
