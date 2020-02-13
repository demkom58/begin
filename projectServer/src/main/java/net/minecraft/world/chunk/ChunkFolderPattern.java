package net.minecraft.world.chunk;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChunkFolderPattern implements FileFilter {
    public static final Pattern PATTERN = Pattern.compile("[0-9a-z]|([0-9a-z][0-9a-z])");

    public ChunkFolderPattern() { }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            Matcher matcher = PATTERN.matcher(file.getName());
            return matcher.matches();
        }

        return false;
    }
}
