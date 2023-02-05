package net.minecraft.client.sound;

import net.hypnosis.audio.Sound;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SoundPoolEntry {
    public final String name;
    public final Sound sound;

    public SoundPoolEntry(@NotNull final String name,
                          @NotNull final Sound sound) {
        this.name = name;
        this.sound = sound;
    }

    @Override
    public String toString() {
        return "SoundPoolEntry{" +
                "name='" + name + '\'' +
                ", sound=" + sound +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoundPoolEntry that = (SoundPoolEntry) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(sound, that.sound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sound);
    }

}
