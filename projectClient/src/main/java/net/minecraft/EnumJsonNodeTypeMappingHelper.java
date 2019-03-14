package net.minecraft;

// $FF: synthetic class
class EnumJsonNodeTypeMappingHelper {
    // $FF: synthetic field
    static final int[] VALUES = new int[EnumJsonNodeType.values().length];

    static {
        try {
            VALUES[EnumJsonNodeType.ARRAY.ordinal()] = 1;
        } catch (NoSuchFieldError error) { }

        try {
            VALUES[EnumJsonNodeType.OBJECT.ordinal()] = 2;
        } catch (NoSuchFieldError error) { }

        try {
            VALUES[EnumJsonNodeType.STRING.ordinal()] = 3;
        } catch (NoSuchFieldError error) { }

        try {
            VALUES[EnumJsonNodeType.NUMBER.ordinal()] = 4;
        } catch (NoSuchFieldError error) { }

        try {
            VALUES[EnumJsonNodeType.FALSE.ordinal()] = 5;
        } catch (NoSuchFieldError error) { }

        try {
            VALUES[EnumJsonNodeType.TRUE.ordinal()] = 6;
        } catch (NoSuchFieldError error) { }

        try {
            VALUES[EnumJsonNodeType.NULL.ordinal()] = 7;
        } catch (NoSuchFieldError error) { }

    }
}
