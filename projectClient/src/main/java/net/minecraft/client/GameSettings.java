package net.minecraft.client;

import net.hypnosis.input.KeySource;
import net.minecraft.client.gui.EnumOption;
import net.minecraft.client.input.keyboard.KeyBinding;
import net.minecraft.stats.StatCollector;
import net.minecraft.util.StringTranslate;
import org.lwjgl.glfw.GLFW;

import java.io.*;

public class GameSettings {
    private static final String[] RENDER_DISTANCES = new String[]{"options.renderDistance.far", "options.renderDistance.normal", "options.renderDistance.short", "options.renderDistance.tiny"};
    private static final String[] DIFFICULTIES = new String[]{"options.difficulty.peaceful", "options.difficulty.easy", "options.difficulty.normal", "options.difficulty.hard"};
    private static final String[] GUISCALES = new String[]{"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
    private static final String[] LIMIT_FRAMERATES = new String[]{"performance.max", "performance.balanced", "performance.powersaver"};
    public float musicVolume = 1.0F;
    public float soundVolume = 1.0F;
    public float mouseSensitivity = 0.5F;
    public boolean invertMouse = false;
    public int renderDistance = 0;
    public boolean viewBobbing = true;
    public boolean anaglyph = false;
    public boolean advancedOpengl = false;
    public int limitFramerate = 1;
    public boolean fancyGraphics = true;
    public boolean ambientOcclusion = true;
    public String skin = "Default";
    public KeyBinding keyBindForward = new KeyBinding("key.forward", GLFW.GLFW_KEY_W);
    public KeyBinding keyBindLeft = new KeyBinding("key.left", GLFW.GLFW_KEY_A);
    public KeyBinding keyBindBack = new KeyBinding("key.back", GLFW.GLFW_KEY_S);
    public KeyBinding keyBindRight = new KeyBinding("key.right", GLFW.GLFW_KEY_D);
    public KeyBinding keyBindJump = new KeyBinding("key.jump", GLFW.GLFW_KEY_SPACE);
    public KeyBinding keyBindInventory = new KeyBinding("key.inventory", GLFW.GLFW_KEY_E);
    public KeyBinding keyBindDrop = new KeyBinding("key.drop", GLFW.GLFW_KEY_Q);
    public KeyBinding keyBindChat = new KeyBinding("key.chat", GLFW.GLFW_KEY_T);
    public KeyBinding keyBindToggleFog = new KeyBinding("key.fog", GLFW.GLFW_KEY_L);
    public KeyBinding keyBindSneak = new KeyBinding("key.sneak", GLFW.GLFW_KEY_LEFT_SHIFT);
    public KeyBinding[] keyBindings;
    public int difficulty;
    public boolean hideGUI;
    public boolean thirdPersonView;
    public boolean showDebugInfo;
    public String lastServer;
    public boolean noclip;
    public boolean smoothCamera;
    public boolean debugCamEnable;
    public float noclipRate;
    public float debugCamRate;
    public int guiScale;
    protected MinecraftClient client;
    private File optionsFile;

    public GameSettings(MinecraftClient client, File settingsRoot) {
        this.keyBindings = new KeyBinding[]{
                this.keyBindForward,
                this.keyBindLeft,
                this.keyBindBack,
                this.keyBindRight,
                this.keyBindJump,
                this.keyBindSneak,
                this.keyBindDrop,
                this.keyBindInventory,
                this.keyBindChat,
                this.keyBindToggleFog
        };

        this.difficulty = 2;
        this.hideGUI = false;
        this.thirdPersonView = false;
        this.showDebugInfo = false;
        this.lastServer = "";
        this.noclip = false;
        this.smoothCamera = false;
        this.debugCamEnable = false;
        this.noclipRate = 1.0F;
        this.debugCamRate = 1.0F;
        this.guiScale = 0;
        this.client = client;
        this.optionsFile = new File(settingsRoot, "options.txt");
        this.loadOptions();
    }

    public GameSettings() {
        this.keyBindings = new KeyBinding[]{
                this.keyBindForward,
                this.keyBindLeft,
                this.keyBindBack,
                this.keyBindRight,
                this.keyBindJump,
                this.keyBindSneak,
                this.keyBindDrop,
                this.keyBindInventory,
                this.keyBindChat,
                this.keyBindToggleFog
        };

        this.difficulty = 2;
        this.hideGUI = false;
        this.thirdPersonView = false;
        this.showDebugInfo = false;
        this.lastServer = "";
        this.noclip = false;
        this.smoothCamera = false;
        this.debugCamEnable = false;
        this.noclipRate = 1.0F;
        this.debugCamRate = 1.0F;
        this.guiScale = 0;
    }

    public String getKeyBindingDescription(int bindingId) {
        StringTranslate translate = StringTranslate.getInstance();
        return translate.translateKey(this.keyBindings[bindingId].keyDescription);
    }

    public String getOptionDisplayString(int bindingId) {
        StringTranslate instance = StringTranslate.getInstance();
        return instance.translateKey(KeySource.KEYBOARD.getKeyInfo(this.keyBindings[bindingId].keyCode).getName());
    }

    public void setKeyBinding(int bindingId, int keyCode, int scanCode) {
        final KeyBinding keyBinding = this.keyBindings[bindingId];
        keyBinding.keyCode = keyCode;
        this.saveOptions();
    }

    public void setOptionFloatValue(EnumOption option, float value) {
        if (option == EnumOption.MUSIC) {
            this.musicVolume = value;
            this.client.soundManager.onSoundOptionsChanged();
        }

        if (option == EnumOption.SOUND) {
            this.soundVolume = value;
            this.client.soundManager.onSoundOptionsChanged();
        }

        if (option == EnumOption.SENSITIVITY) {
            this.mouseSensitivity = value;
        }

    }

    public void setOptionValue(EnumOption option, int value) {
        if (option == EnumOption.INVERT_MOUSE) {
            this.invertMouse = !this.invertMouse;
        }

        if (option == EnumOption.RENDER_DISTANCE) {
            this.renderDistance = this.renderDistance + value & 3;
        }

        if (option == EnumOption.GUI_SCALE) {
            this.guiScale = this.guiScale + value & 3;
        }

        if (option == EnumOption.VIEW_BOBBING) {
            this.viewBobbing = !this.viewBobbing;
        }

        if (option == EnumOption.ADVANCED_OPENGL) {
            this.advancedOpengl = !this.advancedOpengl;
            this.client.renderGlobal.loadRenderers();
        }

        if (option == EnumOption.ANAGLYPH) {
            this.anaglyph = !this.anaglyph;
            this.client.renderEngine.refreshTextures();
        }

        if (option == EnumOption.FRAMERATE_LIMIT) {
            this.limitFramerate = (this.limitFramerate + value + 3) % 3;
        }

        if (option == EnumOption.DIFFICULTY) {
            this.difficulty = this.difficulty + value & 3;
        }

        if (option == EnumOption.GRAPHICS) {
            this.fancyGraphics = !this.fancyGraphics;
            this.client.renderGlobal.loadRenderers();
        }

        if (option == EnumOption.AMBIENT_OCCLUSION) {
            this.ambientOcclusion = !this.ambientOcclusion;
            this.client.renderGlobal.loadRenderers();
        }

        this.saveOptions();
    }

    public float getOptionFloatValue(EnumOption option) {
        if (option == EnumOption.MUSIC)
            return this.musicVolume;

        if (option == EnumOption.SOUND)
            return this.soundVolume;

        return option == EnumOption.SENSITIVITY ? this.mouseSensitivity : 0.0F;
    }

    public boolean getOptionOrdinalValue(EnumOption options) {
        switch (options) {
            case INVERT_MOUSE:
                return this.invertMouse;
            case VIEW_BOBBING:
                return this.viewBobbing;
            case ANAGLYPH:
                return this.anaglyph;
            case ADVANCED_OPENGL:
                return this.advancedOpengl;
            case AMBIENT_OCCLUSION:
                return this.ambientOcclusion;
        }
        return false;
    }

    public String getKeyBinding(EnumOption option) {
        StringTranslate translate = StringTranslate.getInstance();
        String fp = translate.translateKey(option.getKey()) + ": ";
        if (option.getFloatType()) {
            float val = this.getOptionFloatValue(option);
            if (option == EnumOption.SENSITIVITY) {
                if (val == 0.0F)
                    return fp + translate.translateKey("options.sensitivity.min");

                return val == 1.0F ? fp + translate.translateKey("options.sensitivity.max") : fp + (int) (val * 200.0F) + "%";
            }
            return val == 0.0F ? fp + translate.translateKey("options.off") : fp + (int) (val * 100.0F) + "%";
        }

        if (option.getBoolType())
            return this.getOptionOrdinalValue(option) ? fp + translate.translateKey("options.on") : fp + translate.translateKey("options.off");

        if (option == EnumOption.RENDER_DISTANCE)
            return fp + translate.translateKey(RENDER_DISTANCES[this.renderDistance]);

        if (option == EnumOption.DIFFICULTY)
            return fp + translate.translateKey(DIFFICULTIES[this.difficulty]);

        if (option == EnumOption.GUI_SCALE)
            return fp + translate.translateKey(GUISCALES[this.guiScale]);

        if (option == EnumOption.FRAMERATE_LIMIT)
            return fp + StatCollector.translateToLocal(LIMIT_FRAMERATES[this.limitFramerate]);

        if (option == EnumOption.GRAPHICS)
            return this.fancyGraphics ? fp + translate.translateKey("options.graphics.fancy") : fp + translate.translateKey("options.graphics.fast");

        return fp;
    }

    public void loadOptions() {
        try {
            if (!this.optionsFile.exists())
                return;

            BufferedReader reader = new BufferedReader(new FileReader(this.optionsFile));
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    String[] keyValue = line.split(":");
                    if (keyValue[0].equals("music"))
                        this.musicVolume = this.parseFloat(keyValue[1]);

                    if (keyValue[0].equals("sound"))
                        this.soundVolume = this.parseFloat(keyValue[1]);

                    if (keyValue[0].equals("mouseSensitivity"))
                        this.mouseSensitivity = this.parseFloat(keyValue[1]);

                    if (keyValue[0].equals("invertYMouse"))
                        this.invertMouse = keyValue[1].equals("true");

                    if (keyValue[0].equals("viewDistance"))
                        this.renderDistance = Integer.parseInt(keyValue[1]);

                    if (keyValue[0].equals("guiScale"))
                        this.guiScale = Integer.parseInt(keyValue[1]);

                    if (keyValue[0].equals("bobView"))
                        this.viewBobbing = keyValue[1].equals("true");

                    if (keyValue[0].equals("anaglyph3d"))
                        this.anaglyph = keyValue[1].equals("true");

                    if (keyValue[0].equals("advancedOpengl"))
                        this.advancedOpengl = keyValue[1].equals("true");

                    if (keyValue[0].equals("fpsLimit"))
                        this.limitFramerate = Integer.parseInt(keyValue[1]);

                    if (keyValue[0].equals("difficulty"))
                        this.difficulty = Integer.parseInt(keyValue[1]);

                    if (keyValue[0].equals("fancyGraphics"))
                        this.fancyGraphics = keyValue[1].equals("true");

                    if (keyValue[0].equals("ao"))
                        this.ambientOcclusion = keyValue[1].equals("true");

                    if (keyValue[0].equals("skin"))
                        this.skin = keyValue[1];

                    if (keyValue[0].equals("lastServer") && keyValue.length >= 2)
                        this.lastServer = keyValue[1];

                    for (KeyBinding keyBinding : this.keyBindings)
                        if (keyValue[0].equals("key_" + keyBinding.keyDescription))
                            keyBinding.keyCode = Integer.parseInt(keyValue[1]);

                } catch (Exception e) {
                    System.out.println("Skipping bad option: " + line);
                }
            }

            reader.close();
        } catch (Exception e1) {
            System.out.println("Failed to load options");
            e1.printStackTrace();
        }

    }

    private float parseFloat(String str) {
        if (str.equals("true"))
            return 1.0F;

        return str.equals("false") ? 0.0F : Float.parseFloat(str);
    }

    public void saveOptions() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(this.optionsFile));
            writer.println("music:" + this.musicVolume);
            writer.println("sound:" + this.soundVolume);
            writer.println("invertYMouse:" + this.invertMouse);
            writer.println("mouseSensitivity:" + this.mouseSensitivity);
            writer.println("viewDistance:" + this.renderDistance);
            writer.println("guiScale:" + this.guiScale);
            writer.println("bobView:" + this.viewBobbing);
            writer.println("anaglyph3d:" + this.anaglyph);
            writer.println("advancedOpengl:" + this.advancedOpengl);
            writer.println("fpsLimit:" + this.limitFramerate);
            writer.println("difficulty:" + this.difficulty);
            writer.println("fancyGraphics:" + this.fancyGraphics);
            writer.println("ao:" + this.ambientOcclusion);
            writer.println("skin:" + this.skin);
            writer.println("lastServer:" + this.lastServer);

            for (KeyBinding keyBinding : this.keyBindings)
                writer.println("key_" + keyBinding.keyDescription + ":" + keyBinding.keyCode);
            
            writer.close();
        } catch (Exception e) {
            System.out.println("Failed to save options");
            e.printStackTrace();
        }

    }
}
