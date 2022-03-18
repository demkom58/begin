package net.potion.world.storage;

import net.potion.nbt.CompressedStreamTools;
import net.potion.nbt.TagCompound;
import net.potion.world.World;
import net.potion.world.WorldInfo;
import net.potion.world.chunk.OldChunk;
import net.potion.world.chunk.OldChunkLoader;
import net.potion.world.chunk.IOldChunkLoader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class RegionChunkLoader implements IOldChunkLoader {
    private final File worldDir;

    public RegionChunkLoader(File var1) {
        this.worldDir = var1;
    }

    @Override
    public OldChunk loadChunk(World world, int x, int z) throws IOException {
        DataInputStream inputStream = RegionFileCache.getChunkInputStream(this.worldDir, x, z);
        if (inputStream == null) {
            return null;
        }

        TagCompound var5 = CompressedStreamTools.readCompound(inputStream);
        if (!var5.hasKey("Level")) {
            System.out.println("Chunk file at " + x + "," + z + " is missing level data, skipping");
            return null;
        } else if (!var5.getCompoundTag("Level").hasKey("Blocks")) {
            System.out.println("Chunk file at " + x + "," + z + " is missing block data, skipping");
            return null;
        } else {
            OldChunk var6 = OldChunkLoader.loadChunkIntoWorldFromCompound(world, var5.getCompoundTag("Level"));
            if (!var6.isAtLocation(x, z)) {
                System.out.println("Chunk file at " + x + "," + z + " is in the wrong location; relocating. (Expected " + x + ", " + z + ", got " + var6.xPosition + ", " + var6.zPosition + ")");
                var5.setInteger("xPos", x);
                var5.setInteger("zPos", z);
                var6 = OldChunkLoader.loadChunkIntoWorldFromCompound(world, var5.getCompoundTag("Level"));
            }

            var6.checkBlocks();
            return var6;
        }
    }

    @Override
    public void saveChunk(World world, OldChunk chunk) throws IOException {
        world.checkSessionLock();

        try {
            DataOutputStream var3 = RegionFileCache.getChunkOutputStream(this.worldDir, chunk.xPosition, chunk.zPosition);
            TagCompound var4 = new TagCompound();
            TagCompound var5 = new TagCompound();
            var4.setTag("Level", var5);
            OldChunkLoader.storeChunkInCompound(chunk, world, var5);
            CompressedStreamTools.writeCompound(var4, var3);
            var3.close();
            WorldInfo var6 = world.getWorldInfo();
            var6.setSizeOnDisk(var6.getSizeOnDisk() + (long) RegionFileCache.getSizeDelta(this.worldDir, chunk.xPosition, chunk.zPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveExtraChunkData(World world, OldChunk chunk) throws IOException {
    }

    @Override
    public void onUnloadOldest() {
    }

    @Override
    public void saveExtraData() {
    }
}
