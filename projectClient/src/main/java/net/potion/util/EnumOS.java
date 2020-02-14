package net.potion.util;

import java.io.File;

public enum EnumOS {
    LINUX,
    SOLARIS,
    WINDOWS,
    MACOS,
    UNKNOWN;

    public static EnumOS current() {
        final String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win"))
            return EnumOS.WINDOWS;

        if (osName.contains("mac"))
            return EnumOS.MACOS;

        if (osName.contains("solaris"))
            return EnumOS.SOLARIS;

        if (osName.contains("sunos"))
            return EnumOS.SOLARIS;

        if (osName.contains("linux"))
            return EnumOS.LINUX;

        return osName.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN;
    }

    public static File getAppDir(String name) {
        final String userHome = System.getProperty("user.home", ".");

        File localFile;
        switch (EnumOS.current()) {
            case LINUX:
            case SOLARIS:
                localFile = new File(userHome, '.' + name + '/');
                break;
            case WINDOWS:
                String appdata = System.getenv("APPDATA");
                if (appdata != null)
                    localFile = new File(appdata, "." + name + '/');
                else
                    localFile = new File(userHome, '.' + name + '/');
                break;
            case MACOS:
                localFile = new File(userHome, "Library/Application Support/" + name);
                break;
            default:
                localFile = new File(userHome, name + '/');
        }

        if (!localFile.exists() && !localFile.mkdirs())
            throw new RuntimeException("The working directory could not be created: " + localFile);

        return localFile;

    }

}
