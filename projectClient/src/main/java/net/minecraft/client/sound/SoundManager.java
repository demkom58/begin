package net.minecraft.client.sound;

import net.minecraft.client.GameSettings;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;

import java.io.File;
import java.util.Random;

public class SoundManager {
    private static boolean loaded = false;
    private final Random random = new Random();

    private GameSettings options;
    private int ticksBeforeMusic;

//    private static SoundSystem sndSystem;
//    private SoundPool soundPoolSounds = new SoundPool();
//    private SoundPool soundPoolMusic = new SoundPool();
//    private int field_587_e = 0;

    public SoundManager() {
        this.ticksBeforeMusic = this.random.nextInt(12000);
    }

    public void loadSoundSettings(GameSettings settings) {
        this.options = settings;
        if (!loaded && (settings == null || settings.soundVolume != 0.0F || settings.musicVolume != 0.0F))
            this.tryToSetLibraryAndCodecs();
    }

    private void tryToSetLibraryAndCodecs() {
        try {
            float soundVolume = this.options.soundVolume;
            float musicVolume = this.options.musicVolume;
            this.options.soundVolume = 0.0F;
            this.options.musicVolume = 0.0F;
            this.options.saveOptions();
//            SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
//            SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
//            SoundSystemConfig.setCodec("wav", CodecWav.class);
//            sndSystem = new SoundSystem();
            this.options.soundVolume = soundVolume;
            this.options.musicVolume = musicVolume;
            this.options.saveOptions();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.err.println("error linking with the LibraryJavaSound plug-in");
        }

        loaded = true;
    }

    public void onSoundOptionsChanged() {
        if (!loaded && (this.options.soundVolume != 0.0F || this.options.musicVolume != 0.0F))
            this.tryToSetLibraryAndCodecs();

        if (!loaded)
            return;

        if (this.options.musicVolume == 0.0F) {
//            sndSystem.stop("BgMusic");
        } else {
//            sndSystem.setVolume("BgMusic", this.options.musicVolume);
        }

    }

    public void closeMinecraft() {
        if (loaded) {
//            sndSystem.cleanup();
        }
    }

    public void addSound(String var1, File var2) {
//        this.soundPoolSounds.addSound(var1, var2);
    }

    public void addStreaming(String var1, File var2) {
//        this.soundPoolStreaming.addSound(var1, var2);
    }

    public void addMusic(String var1, File var2) {
//        this.soundPoolMusic.addSound(var1, var2);
    }

    public void playRandomMusicIfReady() {
        if (!loaded || this.options.musicVolume == 0.0F)
            return;

//        if (!sndSystem.playing("BgMusic") && !sndSystem.playing("streaming")) {
//            if (this.ticksBeforeMusic > 0) {
//                --this.ticksBeforeMusic;
//                return;
//            }
//
//            SoundPoolEntry var1 = this.soundPoolMusic.getRandomSound();
//            if (var1 != null) {
//                this.ticksBeforeMusic = this.rand.nextInt(12000) + 12000;
//                sndSystem.backgroundMusic("BgMusic", var1.soundUrl, var1.soundName, false);
//                sndSystem.setVolume("BgMusic", this.options.musicVolume);
//                sndSystem.play("BgMusic");
//            }
//        }
    }

    public void func_338_a(EntityLiving entity, float radius) {
        if (!loaded && this.options.soundVolume == 0.0F)
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
        float zero1 = 0.0F;
        float notCos = -cos;
        float zero2 = 0.0F;
        float one = 1.0F;
        float zero3 = 0.0F;

//        sndSystem.setListenerPosition((float) x, (float) y, (float) z);
//        sndSystem.setListenerOrientation(notSin, zero1, notCos, zero2, one, zero3);
    }

    public void playStreaming(String var1, float var2, float var3, float var4, float var5, float var6) {
        if (!loaded || this.options.soundVolume == 0.0F)
            return;

//        String var7 = "streaming";
//        if (sndSystem.playing("streaming")) {
//            sndSystem.stop("streaming");
//        }
//
//        if (var1 != null) {
//            SoundPoolEntry var8 = this.soundPoolStreaming.getRandomSoundFromSoundPool(var1);
//            if (var8 != null && var5 > 0.0F) {
//                if (sndSystem.playing("BgMusic")) {
//                    sndSystem.stop("BgMusic");
//                }
//
//                float var9 = 16.0F;
//                sndSystem.newStreamingSource(true, var7, var8.soundUrl, var8.soundName, false, var2, var3, var4, 2, var9 * 4.0F);
//                sndSystem.setVolume(var7, 0.5F * this.options.soundVolume);
//                sndSystem.play(var7);
//            }
//
//        }
    }

    public void playSound(String var1, float var2, float var3, float var4, float var5, float var6) {
        if (!loaded || this.options.soundVolume == 0.0F)
            return;

//        SoundPoolEntry var7 = this.soundPoolSounds.getRandomSoundFromSoundPool(var1);
//        if (var7 != null && var5 > 0.0F) {
//            this.field_587_e = (this.field_587_e + 1) % 256;
//            String var8 = "sound_" + this.field_587_e;
//            float var9 = 16.0F;
//            if (var5 > 1.0F) {
//                var9 *= var5;
//            }
//
//            sndSystem.newSource(var5 > 1.0F, var8, var7.soundUrl, var7.soundName, false, var2, var3, var4, 2, var9);
//            sndSystem.setPitch(var8, var6);
//            if (var5 > 1.0F) {
//                var5 = 1.0F;
//            }
//
//            sndSystem.setVolume(var8, var5 * this.options.soundVolume);
//            sndSystem.play(var8);
//        }
    }

    public void playSoundFX(String var1, float var2, float var3) {
        if (!loaded || this.options.soundVolume == 0.0F)
            return;

//        SoundPoolEntry var4 = this.soundPoolSounds.getRandomSoundFromSoundPool(var1);
//        if (var4 == null)
//            return;
//
//        this.field_587_e = (this.field_587_e + 1) % 256;
//        String var5 = "sound_" + this.field_587_e;
//        sndSystem.newSource(false, var5, var4.soundUrl, var4.soundName, false, 0.0F, 0.0F, 0.0F, 0, 0.0F);
//        if (var2 > 1.0F)
//            var2 = 1.0F;
//
//        var2 = var2 * 0.25F;
//        sndSystem.setPitch(var5, var3);
//        sndSystem.setVolume(var5, var2 * this.options.soundVolume);
//        sndSystem.play(var5);
    }
}
