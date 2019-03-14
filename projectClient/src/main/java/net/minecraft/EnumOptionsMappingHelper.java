package net.minecraft;

// $FF: synthetic class
class EnumOptionsMappingHelper {
    // $FF: synthetic field
    static final int[] VALUES = new int[EnumOptions.values().length];

    static {
        try {
            VALUES[EnumOptions.INVERT_MOUSE.ordinal()] = 1;
        } catch (NoSuchFieldError error) { }

        try {
            VALUES[EnumOptions.VIEW_BOBBING.ordinal()] = 2;
        } catch (NoSuchFieldError error) { }

        try {
            VALUES[EnumOptions.ANAGLYPH.ordinal()] = 3;
        } catch (NoSuchFieldError error) { }

        try {
            VALUES[EnumOptions.ADVANCED_OPENGL.ordinal()] = 4;
        } catch (NoSuchFieldError error) { }

        try {
            VALUES[EnumOptions.AMBIENT_OCCLUSION.ordinal()] = 5;
        } catch (NoSuchFieldError error) { }

    }
}
