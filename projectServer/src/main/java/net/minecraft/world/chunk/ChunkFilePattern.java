package net.minecraft.world.chunk;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChunkFilePattern implements FilenameFilter {
    public static final Pattern PATTERN = Pattern.compile("c\\.(-?[0-9a-z]+)\\.(-?[0-9a-z]+)\\.dat");

    public ChunkFilePattern() {
    }

    @Override
    public boolean accept(File dir, String val) {
        Matcher matcher = PATTERN.matcher(val);
        return matcher.matches();
    }
}
