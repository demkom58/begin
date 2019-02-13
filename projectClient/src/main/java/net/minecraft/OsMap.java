package net.minecraft;

// $FF: synthetic class
class OsMap {
    // $FF: synthetic field
    static final int[] field_1193_a = new int[EnumOS1.values().length];

    static {
        try {
            field_1193_a[EnumOS1.LINUX.ordinal()] = 1;
        } catch (NoSuchFieldError var4) {
            ;
        }

        try {
            field_1193_a[EnumOS1.SOLARIS.ordinal()] = 2;
        } catch (NoSuchFieldError var3) {
            ;
        }

        try {
            field_1193_a[EnumOS1.WINDOWS.ordinal()] = 3;
        } catch (NoSuchFieldError var2) {
            ;
        }

        try {
            field_1193_a[EnumOS1.MACOS.ordinal()] = 4;
        } catch (NoSuchFieldError var1) {
            ;
        }

    }
}
