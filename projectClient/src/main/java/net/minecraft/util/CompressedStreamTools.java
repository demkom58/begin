package net.minecraft.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedStreamTools {
    public static NBTTagCompound readGzipCompound(InputStream stream) throws IOException {
        DataInputStream inputStream = new DataInputStream(new GZIPInputStream(stream));

        NBTTagCompound compound;
        try {
            compound = readCompound(inputStream);
        } finally {
            inputStream.close();
        }

        return compound;
    }

    public static void writeGzipCompound(NBTTagCompound compound, OutputStream stream)
            throws IOException {
        try (DataOutputStream outputStream = new DataOutputStream(new GZIPOutputStream(stream))) {
            writeCompound(compound, outputStream);
        }
    }

    public static NBTTagCompound readCompound(DataInput input) throws IOException {
        NBTBase base = NBTBase.readTag(input);

        if (base instanceof NBTTagCompound)
            return (NBTTagCompound) base;

        throw new IOException("Root tag must be a named compound tag");
    }

    public static void writeCompound(NBTTagCompound compound, DataOutput output) throws IOException {
        NBTBase.writeTag(compound, output);
    }
}
