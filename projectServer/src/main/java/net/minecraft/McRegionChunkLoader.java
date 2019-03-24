package net.minecraft;

import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class McRegionChunkLoader implements IChunkLoader {
    private final File worldFolder;

    public McRegionChunkLoader(File worldFolder) {
        this.worldFolder = worldFolder;
    }

    public Chunk loadChunk(World world, int x, int z) throws IOException {
        DataInputStream inputStream = RegionFileCache.getChunkInputStream(this.worldFolder, x, z);
        if (inputStream != null) {
            NBTTagCompound var5 = CompressedStreamTools.func_774_a(inputStream);
            if (!var5.hasKey("Level")) {
                System.out.println("Chunk file at " + x + "," + z + " is missing level data, skipping");
                return null;
            }

            if (!var5.getCompoundTag("Level").hasKey("Blocks")) {
                System.out.println("Chunk file at " + x + "," + z + " is missing block data, skipping");
                return null;
            }

            Chunk var6 = ChunkLoader.loadChunkIntoWorldFromCompound(world, var5.getCompoundTag("Level"));
            if (!var6.isAtLocation(x, z)) {
                System.out.println("Chunk file at " + x + "," + z + " is in the wrong location; relocating. (Expected " + x + ", " + z + ", got " + var6.xPosition + ", " + var6.zPosition + ")");
                var5.setInteger("xPos", x);
                var5.setInteger("zPos", z);
                var6 = ChunkLoader.loadChunkIntoWorldFromCompound(world, var5.getCompoundTag("Level"));
            }

            var6.checkBlocks();
            return var6;
        }

        return null;
    }

    public void saveChunk(World world, Chunk chunk) throws IOException {
        world.checkSessionLock();

        try {
            DataOutputStream var3 = RegionFileCache.getChunkOutputStream(this.worldFolder, chunk.xPosition, chunk.zPosition);
            NBTTagCompound var4 = new NBTTagCompound();
            NBTTagCompound var5 = new NBTTagCompound();
            var4.setTag("Level", var5);
            ChunkLoader.storeChunkInCompound(chunk, world, var5);
            CompressedStreamTools.func_771_a(var4, var3);
            var3.close();
            WorldInfo var6 = world.getWorldInfo();
            var6.setSizeOnDisk(var6.getSizeOnDisk() + (long) RegionFileCache.getSizeDelta(this.worldFolder, chunk.xPosition, chunk.zPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveExtraChunkData(World world, Chunk chunk) throws IOException {
    }

    public void func_661_a() {
    }

    public void saveExtraData() {
    }
}
