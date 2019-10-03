package net.hypnosis.audio;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.openal.AL10;

public class Source {
    private final int sourcePointer;
    private final Sound sound;

    public Source(int sourcePointer,
                  @NotNull final Sound sound) {
        this.sourcePointer = sourcePointer;
        this.sound = sound;
    }

    public int getSourcePointer() {
        return sourcePointer;
    }

    public Sound getSound() {
        return sound;
    }

    public void setVelocity(float velocity) {
        AL10.alSourcef(sourcePointer, AL10.AL_VELOCITY, velocity);
    }

    public void setLoop(boolean loop) {
        AL10.alSourcef(sourcePointer, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    public void setVolume(float volume) {
        AL10.alSourcef(sourcePointer, AL10.AL_GAIN, volume);
    }

    public void setPitch(float pitch) {
        AL10.alSourcef(sourcePointer, AL10.AL_PITCH, pitch);
    }

    public void setLocation(float x, float y, float z) {
        AL10.alSource3f(sourcePointer, AL10.AL_POSITION, x, y, z);
    }

    public void setRolloffFactor(float factor) {
        AL10.alSourcef(sourcePointer, AL10.AL_ROLLOFF_FACTOR, factor);
    }

    public void setReferenceDistance(float distance) {
        AL10.alSourcef(sourcePointer, AL10.AL_REFERENCE_DISTANCE, distance);
    }

    public void setMaxDistance(float maxDistance) {
        AL10.alSourcef(sourcePointer, AL10.AL_MAX_DISTANCE, maxDistance);
    }

    public boolean isPlaying() {
        return AL10.alGetSourcei(sourcePointer, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    public void pause() {
        AL10.alSourcePause(sourcePointer);
    }

    public void stop() {
        AL10.alSourceStop(sourcePointer);
    }

    public void resume() {
        AL10.alSourcePlay(sourcePointer);
    }

    public void dispose() {
        stop();
        AL10.alDeleteSources(sourcePointer);
    }

}
