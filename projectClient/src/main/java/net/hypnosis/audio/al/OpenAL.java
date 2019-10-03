package net.hypnosis.audio.al;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.openal.*;

import java.util.Objects;

public class OpenAL {

    public static @NotNull ALCapabilities createCapabilitiesAL(@NotNull ALCCapabilities alcCapabilities) {
        return AL.createCapabilities(alcCapabilities);
    }

    public static @NotNull ALCCapabilities createCapabilitiesALC(long device) {
        return ALC.createCapabilities(device);
    }

    public static void makeCurrentContext(long context) {
        ALC10.alcMakeContextCurrent(context);
    }

    public static long createContext(long device) {
        return OpenAL.createContext(device, new int[]{0});
    }

    public static long createContext(long device, @Nullable int[] attributes) {
        return ALC10.alcCreateContext(device, attributes);
    }

    public static long createDefaultDevice() {
        return OpenAL.createDevice(Objects.requireNonNull(getDefaultDeviceName()));
    }

    public static @Nullable String getDefaultDeviceName() {
        return ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
    }

    public static long createDevice(@Nullable final String deviceName) {
        return ALC10.alcOpenDevice(deviceName);
    }

    public static void destroyContext(long context) {
        ALC10.alcDestroyContext(context);
    }

    public static void closeDevice(long device) {
        ALC10.alcCloseDevice(device);
    }

}
