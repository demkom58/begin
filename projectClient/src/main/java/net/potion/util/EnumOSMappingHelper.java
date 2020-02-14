package net.potion.util;

public class EnumOSMappingHelper {
    public static final int[] OS_MAPPING_ARRAY = new int[EnumOS.values().length];

    static {
        try {
            OS_MAPPING_ARRAY[EnumOS.LINUX.ordinal()] = 1;
        } catch (NoSuchFieldError ignored) {
        }

        try {
            OS_MAPPING_ARRAY[EnumOS.SOLARIS.ordinal()] = 2;
        } catch (NoSuchFieldError ignored) {
        }

        try {
            OS_MAPPING_ARRAY[EnumOS.WINDOWS.ordinal()] = 3;
        } catch (NoSuchFieldError ignored) {
        }

        try {
            OS_MAPPING_ARRAY[EnumOS.MACOS.ordinal()] = 4;
        } catch (NoSuchFieldError ignored) {
        }

    }
}
