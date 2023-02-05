package net.minecraft.client.gui;

public enum EnumOption {
    MUSIC("options.music", true, false),
    SOUND("options.sound", true, false),
    INVERT_MOUSE("options.invertMouse", false, true),
    SENSITIVITY("options.sensitivity", true, false),
    RENDER_DISTANCE("options.renderDistance", false, false),
    VIEW_BOBBING("options.viewBobbing", false, true),
    ANAGLYPH("options.anaglyph", false, true),
    ADVANCED_OPENGL("options.advancedOpengl", false, true),
    FRAMERATE_LIMIT("options.framerateLimit", false, false),
    DIFFICULTY("options.difficulty", false, false),
    GRAPHICS("options.graphics", false, false),
    AMBIENT_OCCLUSION("options.ao", false, true),
    GUI_SCALE("options.guiScale", false, false);

    private final String key;
    private final boolean floatType;
    private final boolean boolType;

    EnumOption(String key, boolean floatType, boolean boolType) {
        this.key = key;
        this.floatType = floatType;
        this.boolType = boolType;
    }

    public static EnumOption getEnumOptions(int i) {
        for (EnumOption option : values())
            if (option.ordinal() == i)
                return option;

        return null;
    }

    public String getKey() {
        return this.key;
    }

    public boolean getFloatType() {
        return this.floatType;
    }

    public boolean getBoolType() {
        return this.boolType;
    }

}
