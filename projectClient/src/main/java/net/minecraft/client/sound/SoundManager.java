package net.minecraft.client.sound;

import net.hypnosis.audio.Sound;
import net.hypnosis.audio.SoundSystem;
import net.hypnosis.audio.Source;
import net.minecraft.client.GameSettings;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;

import java.io.File;
import java.util.Random;

public class SoundManager {
    private static boolean loaded = true;
    private final Random random = new Random();

    private final SoundSystem soundSystem;

    private final SoundPool soundsPool;
    private final SoundPool streamPool;
    private final SoundPool musicPool;

    private GameSettings settings;
    private int ticksBeforeMusic;

    public SoundManager() {
        this.ticksBeforeMusic = this.random.nextInt(12000);

        this.soundSystem = new SoundSystem();

        this.soundsPool = new SoundPool(soundSystem);
        this.streamPool = new SoundPool(soundSystem);
        this.musicPool = new SoundPool(soundSystem);
    }

    public void tick() {
        this.soundsPool.tick();
        this.streamPool.tick();
        this.musicPool.tick();
    }

    public void setSettings(GameSettings settings) {
        this.settings = settings;
    }

    public void onSoundOptionsChanged() {
        if (!loaded)
            return;

        if (this.settings.musicVolume == 0.0F) {
            musicPool.stop("BgMusic");
            return;
        }

        Source bgMusic = musicPool.getPlaying("BgMusic");
        if (bgMusic != null)
            bgMusic.setVolume(this.settings.musicVolume);
    }

    public void closeMinecraft() {
        if (!loaded)
            return;

        musicPool.getSoundEntries().forEach(entry -> entry.sound.dispose());
        soundSystem.dispose();
    }

    public void addSound(String resource, File file) {
        Sound sound = this.soundSystem.load(file);
        this.soundsPool.addSound(resource, sound);
    }

    public void addStreaming(String resource, File file) {
        Sound sound = this.soundSystem.load(file);
        this.streamPool.addSound(resource, sound);
    }

    public void addMusic(String resource, File file) {
        Sound sound = this.soundSystem.load(file);
        this.musicPool.addSound(resource, sound);
    }

    public void playRandomMusicIfReady() {
        if (!loaded || this.settings.musicVolume == 0.0F)
            return;

        if (musicPool.isPlaying("BgMusic") || streamPool.isPlaying("streaming"))
            return;

        if (this.ticksBeforeMusic > 0) {
            --this.ticksBeforeMusic;
            return;
        }

        SoundPoolEntry entry = this.musicPool.getRandomSound();
        if (entry == null)
            return;

        Source source = soundSystem.createSource(entry.sound);
        source.setVolume(this.settings.musicVolume);
        musicPool.play("BgMusic", source);
    }

    public void setListenerData(EntityLiving entity, float radius) {
        if (!loaded && this.settings.soundVolume == 0.0F)
            return;

        if (entity == null)
            return;

        float radians = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * radius;
        double x = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) radius;
        double y = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) radius;
        double z = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) radius;

        float cos = MathHelper.cos(-radians * 0.017453292F - 3.1415927F);
        float sin = MathHelper.sin(-radians * 0.017453292F - 3.1415927F);

        float notSin = -sin;
        float notCos = -cos;

        this.soundSystem.setListenerLocation((float) x, (float) y, (float) z);
        this.soundSystem.setListenerOrientation(notSin, 0f, notCos, 0f, 1f, 0f);
    }

    public void playStreaming(String soundCategory, float x, float y, float z, float volume, float pitch) {
        if (!loaded || this.settings.soundVolume == 0.0F)
            return;

        streamPool.stop("streaming");
        if (soundCategory == null)
            return;

        SoundPoolEntry entry = this.streamPool.getRandomSound(soundCategory);
        if (entry == null || volume <= 0.0F)
            return;

        musicPool.stop("BgMusic");

        Source source = soundSystem.createSource(entry.sound);
        source.setVolume(0.5F * this.settings.soundVolume);
        source.setPitch(pitch);
        source.setLocation(x, y, z);
        this.streamPool.play(source);
    }

    public void playSound(String soundCategory, float x, float y, float z, float volume, float pitch) {
        if (!loaded || this.settings.soundVolume == 0.0F)
            return;

        SoundPoolEntry poolEntry = this.soundsPool.getRandomSound(soundCategory);
        if (poolEntry == null || volume <= 0.0F)
            return;

        Source source = this.soundSystem.createSource(poolEntry.sound);
        source.setPitch(pitch);
        source.setLocation(x, y, z);

        if (volume > 1.0F)
            volume = 1.0F;

        source.setVolume(volume * this.settings.soundVolume);
        this.soundsPool.play(source);
    }

    public void playSoundFX(String category, float volume, float pitch) {
        if (!loaded || this.settings.soundVolume == 0.0F)
            return;

        SoundPoolEntry entry = this.soundsPool.getRandomSound(category);
        if (entry == null)
            return;

        Source source = this.soundSystem.createSource(entry.sound);
        if (volume > 1.0F)
            volume = 1.0F;

        volume = volume * 0.25F;
        source.setPitch(pitch);
        source.setVolume(volume * this.settings.soundVolume);
        this.soundsPool.play(source);
    }

}
