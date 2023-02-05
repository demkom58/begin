package net.minecraft.nbt;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedStreamTools {
    public static TagCompound readGzipCompound(InputStream stream) throws IOException {
        DataInputStream inputStream = new DataInputStream(new GZIPInputStream(stream));

        TagCompound compound;
        try {
            compound = readCompound(inputStream);
        } finally {
            inputStream.close();
        }

        return compound;
    }

    public static void writeGzipCompound(TagCompound compound, OutputStream stream)
            throws IOException {
        try (DataOutputStream outputStream = new DataOutputStream(new GZIPOutputStream(stream))) {
            writeCompound(compound, outputStream);
        }
    }

    public static TagCompound readCompound(DataInput input) throws IOException {
        Tag base = Tag.readTag(input);

        if (base instanceof TagCompound)
            return (TagCompound) base;

        throw new IOException("Root tag must be a named compound tag");
    }

    public static void writeCompound(TagCompound compound, DataOutput output) throws IOException {
        Tag.writeTag(compound, output);
    }
}
