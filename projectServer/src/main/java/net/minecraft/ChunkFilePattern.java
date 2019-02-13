package net.minecraft;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ChunkFilePattern implements FilenameFilter {
    public static final Pattern field_22119_a = Pattern.compile("c\\.(-?[0-9a-z]+)\\.(-?[0-9a-z]+)\\.dat");

    public ChunkFilePattern() {
    }

    public boolean accept(File var1, String var2) {
        Matcher var3 = field_22119_a.matcher(var2);
        return var3.matches();
    }
}
