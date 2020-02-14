package net.potion.block;

public class StepSound {
    public final String name;
    public final float volume;
    public final float pitch;

    public StepSound(String name, float volume, float pitch) {
        this.name = name;
        this.volume = volume;
        this.pitch = pitch;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    public String stepSoundDir() {
        return "step." + this.name;
    }

    public String getFormattedName() {
        return "step." + this.name;
    }
}
