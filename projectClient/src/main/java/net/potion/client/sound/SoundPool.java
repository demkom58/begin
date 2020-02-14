package net.potion.client.sound;

import net.hypnosis.audio.Sound;
import net.hypnosis.audio.SoundSystem;
import net.hypnosis.audio.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SoundPool {
    private final Random rand = new Random();

    private final Map<String, List<SoundPoolEntry>> categoryEntryMap = new HashMap<>();
    private final List<SoundPoolEntry> entries = new ArrayList<>();
    private final Map<String, Source> playing = new HashMap<>();

    private final SoundSystem soundSystem;

    public boolean categoryNameWithoutDigits = true;
    private int registeredSize = 0;
    private int counter = 0;

    public SoundPool(@NotNull final SoundSystem soundSystem) {
        this.soundSystem = soundSystem;
    }

    public @NotNull SoundPoolEntry addSound(@NotNull final String soundName,
                                            @NotNull final Sound sound) {
        String validCategory = soundName.substring(0, soundName.indexOf("."));
        boolean numberString = validCategory.chars().allMatch(Character::isDigit);

        if (this.categoryNameWithoutDigits && !numberString)
            while (Character.isDigit(validCategory.charAt(validCategory.length() - 1)))
                validCategory = validCategory.substring(0, validCategory.length() - 1);

        validCategory = validCategory.replaceAll("/", ".");

        this.categoryEntryMap.putIfAbsent(validCategory, new ArrayList<>());
        SoundPoolEntry entry = new SoundPoolEntry(soundName, sound);
        this.categoryEntryMap.get(validCategory).add(entry);

        this.entries.add(entry);
        ++this.registeredSize;

        return entry;
    }

    public @Nullable SoundPoolEntry getRandomSound(@Nullable final String category) {
        List<SoundPoolEntry> entries = this.categoryEntryMap.get(category);
        return entries == null
                ? null
                : entries.get(this.rand.nextInt(entries.size()));
    }

    public @Nullable SoundPoolEntry getRandomSound() {
        return this.entries.size() == 0
                ? null
                : this.entries.get(this.rand.nextInt(this.entries.size()));
    }

    public int getSoundsSize() {
        return registeredSize;
    }

    public @NotNull String play(@NotNull final Source source) {
        String name = source.getSound().getName() + "#" + (counter++);
        this.play(name, source);
        return name;
    }

    public void play(@NotNull final String name, @NotNull final Source source) {
        final Source put = this.playing.put(name, source);

        if (put != null)
            put.dispose();

        this.soundSystem.play(source);
    }

    public void tick() {
        this.playing.entrySet().removeIf(entry -> {
            Source source = entry.getValue();

            if (source.isPlaying())
                return false;

            source.dispose();
            return true;
        });
    }

    public boolean isPlaying(@NotNull final String name) {
        Source source = this.playing.get(name);
        if (source == null)
            return false;

        return source.isPlaying();
    }

    public boolean stop(@NotNull final String name) {
        Source source = playing.get(name);
        if (source == null)
            return false;

        if (!source.isPlaying()) {
            playing.remove(name);
            return false;
        }

        playing.remove(name);
        source.dispose();
        return true;
    }

    public @Nullable Source getPlaying(@NotNull final String name) {
        return playing.get(name);
    }

    public @NotNull List<SoundPoolEntry> getSoundEntries() {
        return Collections.unmodifiableList(entries);
    }
}
