package net.minecraft;

public class EnumOSMappingHelper {
    public static final int[] enumOSMappingArray = new int[EnumOS2.values().length];

    static {
        try {
            enumOSMappingArray[EnumOS2.LINUX.ordinal()] = 1;
        } catch (NoSuchFieldError var4) {
            ;
        }

        try {
            enumOSMappingArray[EnumOS2.SOLARIS.ordinal()] = 2;
        } catch (NoSuchFieldError var3) {
            ;
        }

        try {
            enumOSMappingArray[EnumOS2.WINDOWS.ordinal()] = 3;
        } catch (NoSuchFieldError var2) {
            ;
        }

        try {
            enumOSMappingArray[EnumOS2.MACOS.ordinal()] = 4;
        } catch (NoSuchFieldError var1) {
            ;
        }

    }
}
