package net.potion.world.storage;

import net.potion.world.chunk.Chunk;
import net.potion.world.chunk.ChunkLoader;
import net.potion.world.chunk.IChunkLoader;
import net.potion.nbt.TagCompound;
import net.potion.nbt.CompressedStreamTools;
import net.potion.world.World;
import net.potion.world.WorldInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class RegionChunkLoader implements IChunkLoader {
    private final File worldDir;

    public RegionChunkLoader(File var1) {
        this.worldDir = var1;
    }

    @Override
    public Chunk loadChunk(World var1, int var2, int var3) throws IOException {
        DataInputStream inputStream = RegionFileCache.getChunkInputStream(this.worldDir, var2, var3);
        if (inputStream != null) {
            TagCompound var5 = CompressedStreamTools.readCompound(inputStream);
            if (!var5.hasKey("Level")) {
                System.out.println("Chunk file at " + var2 + "," + var3 + " is missing level data, skipping");
                return null;
            } else if (!var5.getCompoundTag("Level").hasKey("Blocks")) {
                System.out.println("Chunk file at " + var2 + "," + var3 + " is missing block data, skipping");
                return null;
            } else {
                Chunk var6 = ChunkLoader.loadChunkIntoWorldFromCompound(var1, var5.getCompoundTag("Level"));
                if (!var6.isAtLocation(var2, var3)) {
                    System.out.println("Chunk file at " + var2 + "," + var3 + " is in the wrong location; relocating. (Expected " + var2 + ", " + var3 + ", got " + var6.xPosition + ", " + var6.zPosition + ")");
                    var5.setInteger("xPos", var2);
                    var5.setInteger("zPos", var3);
                    var6 = ChunkLoader.loadChunkIntoWorldFromCompound(var1, var5.getCompoundTag("Level"));
                }

                var6.func_25124_i();
                return var6;
            }
        } else {
            return null;
        }
    }

    @Override
    public void saveChunk(World world, Chunk chunk) throws IOException {
        world.checkSessionLock();

        try {
            DataOutputStream var3 = RegionFileCache.getChunkOutputStream(this.worldDir, chunk.xPosition, chunk.zPosition);
            TagCompound var4 = new TagCompound();
            TagCompound var5 = new TagCompound();
            var4.setTag("Level", var5);
            ChunkLoader.storeChunkInCompound(chunk, world, var5);
            CompressedStreamTools.writeCompound(var4, var3);
            var3.close();
            WorldInfo var6 = world.getWorldInfo();
            var6.setSizeOnDisk(var6.getSizeOnDisk() + (long) RegionFileCache.getSizeDelta(this.worldDir, chunk.xPosition, chunk.zPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveExtraChunkData(World var1, Chunk var2) throws IOException {
    }

    @Override
    public void func_814_a() {
    }

    @Override
    public void saveExtraData() {
    }
}
