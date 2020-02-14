package net.potion.json;

// $FF: synthetic class
class EnumJsonNodeTypeMappingHelper {
    // $FF: synthetic field
    static final int[] VALUES = new int[EnumJsonNodeType.values().length];

    static {
        try {
            VALUES[EnumJsonNodeType.ARRAY.ordinal()] = 1;
        } catch (NoSuchFieldError ignored) { }

        try {
            VALUES[EnumJsonNodeType.OBJECT.ordinal()] = 2;
        } catch (NoSuchFieldError ignored) { }

        try {
            VALUES[EnumJsonNodeType.STRING.ordinal()] = 3;
        } catch (NoSuchFieldError ignored) { }

        try {
            VALUES[EnumJsonNodeType.NUMBER.ordinal()] = 4;
        } catch (NoSuchFieldError ignored) { }

        try {
            VALUES[EnumJsonNodeType.FALSE.ordinal()] = 5;
        } catch (NoSuchFieldError ignored) { }

        try {
            VALUES[EnumJsonNodeType.TRUE.ordinal()] = 6;
        } catch (NoSuchFieldError ignored) { }

        try {
            VALUES[EnumJsonNodeType.NULL.ordinal()] = 7;
        } catch (NoSuchFieldError ignored) { }

    }
}
