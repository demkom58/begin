package net.minecraft;

public enum EnumOptions {
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

    private final boolean enumFloat;
    private final boolean enumBoolean;
    private final String enumString;

    EnumOptions(String enumString, boolean enumFloat, boolean enumBoolean) {
        this.enumString = enumString;
        this.enumFloat = enumFloat;
        this.enumBoolean = enumBoolean;
    }

    public static EnumOptions getEnumOptions(int var0) {
        for (EnumOptions options : values()) {
            if (options.returnEnumOrdinal() == var0) {
                return options;
            }
        }

        return null;
    }

    public boolean getEnumFloat() {
        return this.enumFloat;
    }

    public boolean getEnumBoolean() {
        return this.enumBoolean;
    }

    public int returnEnumOrdinal() {
        return this.ordinal();
    }

    public String getEnumString() {
        return this.enumString;
    }
}
