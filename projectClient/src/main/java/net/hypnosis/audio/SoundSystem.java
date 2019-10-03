package net.hypnosis.audio;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.hypnosis.audio.al.OpenAL;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class SoundSystem {
    private final AtomicInteger idCounter = new AtomicInteger();
    private final Int2ObjectMap<Sound> pointerSoundMap = new Int2ObjectOpenHashMap<>();

    private final long pointer;
    private final long contextPointer;

    private ALCCapabilities alcCapabilities;
    private ALCapabilities alCapabilities;

    private boolean contextDestroyed = false;
    private boolean deviceClosed = false;

    public SoundSystem() {
        this.pointer = OpenAL.createDevice(OpenAL.getDefaultDeviceName());
        if (this.pointer == MemoryUtil.NULL)
            throw new IllegalStateException("Created device is null");

        this.contextPointer = OpenAL.createContext(pointer);
        if (this.contextPointer == MemoryUtil.NULL)
            throw new IllegalStateException("Created context is null");

        this.makeCurrentContext();

        this.alcCapabilities = OpenAL.createCapabilitiesALC(pointer);
        this.alCapabilities = OpenAL.createCapabilitiesAL(alcCapabilities);

        if (!this.alCapabilities.OpenAL10)
            System.err.println("*** OpenAL 1.0 is not supported");
    }

    private void makeCurrentContext() {
        OpenAL.makeCurrentContext(this.contextPointer);
    }

    private boolean destroyContext() {
        if (this.contextDestroyed)
            return false;

        OpenAL.destroyContext(this.contextPointer);
        return this.contextDestroyed = true;
    }

    private boolean closeDevice() {
        if (this.deviceClosed)
            return false;

        OpenAL.closeDevice(this.pointer);
        return this.deviceClosed = true;
    }

    public void dispose() {
        if (!contextDestroyed)
            destroyContext();

        if (!deviceClosed)
            closeDevice();
    }

    public Sound load(@NotNull final File file) {
        ShortBuffer rawAudioBuffer;
        int channels;
        int sampleRate;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer channelsBuffer = stack.mallocInt(1);
            IntBuffer sampleRateBuffer = stack.mallocInt(1);

            rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(file.getAbsolutePath(), channelsBuffer, sampleRateBuffer);

            channels = channelsBuffer.get(0);
            sampleRate = sampleRateBuffer.get(0);
        }

        int format = -1;
        if (channels == 1)
            format = AL10.AL_FORMAT_MONO16;
        else if (channels == 2)
            format = AL10.AL_FORMAT_STEREO16;

        int bufferPointer = AL10.alGenBuffers();
        AL10.alBufferData(bufferPointer, format, Objects.requireNonNull(rawAudioBuffer), sampleRate);
        MemoryUtil.memFree(rawAudioBuffer);

        int id = idCounter.getAndIncrement();
        Sound sound = new Sound(id, file.getName(), channels, sampleRate, format, bufferPointer);
        pointerSoundMap.put(id, sound);

        return sound;
    }

    public void disposeSounds() {
        this.pointerSoundMap.values().forEach(Sound::dispose);
        this.pointerSoundMap.clear();
    }

    public Source createSource(@NotNull final Sound sound) {
        int sourcePointer = AL10.alGenSources();
        AL10.alSourcei(sourcePointer, AL10.AL_BUFFER, sound.getBufferPointer());
        return new Source(sourcePointer, sound);
    }

    public void play(@NotNull final Source source) {
        AL10.alSourcePlay(source.getSourcePointer());
    }

    public void setListenerLocation(float x, float y, float z) {
        AL10.alListener3f(AL10.AL_POSITION, x, y, z);
    }

    public void setListenerOrientation(float lookX, float lookY, float lookZ, float upX, float upY, float upZ) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(6);
            buffer.put(0, lookX);
            buffer.put(1, lookY);
            buffer.put(2, lookZ);
            buffer.put(3, upX);
            buffer.put(4, upY);
            buffer.put(5, upZ);
            AL10.alListenerfv(AL10.AL_ORIENTATION, buffer);
        }

    }

    public void setListenerVelocity(float x, float y, float z) {
        AL10.alListener3f(AL10.AL_VELOCITY, x, y, z);
    }

}
