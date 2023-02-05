package net.minecraft.world.storage;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.TagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldInfo;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkLoader;
import net.minecraft.world.chunk.IChunkLoader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class RegionChunkLoader implements IChunkLoader {
    private final File worldDir;

    public RegionChunkLoader(File worldDir) {
        this.worldDir = worldDir;
    }

    @Override
    public Chunk loadChunk(World world, int x, int z) throws IOException {
        DataInputStream inputStream = RegionFileCache.getChunkInputStream(this.worldDir, x, z);
        if (inputStream == null) {
            return null;
        }

        TagCompound rootTag = CompressedStreamTools.readCompound(inputStream);
        if (!rootTag.hasKey("Level")) {
            System.out.println("Chunk file at " + x + "," + z + " is missing level data, skipping");
            return null;
        }

        if (!rootTag.getCompoundTag("Level").hasKey("Blocks")) {
            System.out.println("Chunk file at " + x + "," + z + " is missing block data, skipping");
            return null;
        }

        Chunk chunk = ChunkLoader.loadChunkIntoWorldFromCompound(world, rootTag.getCompoundTag("Level"));
        if (!chunk.isAtLocation(x, z)) {
            System.out.println("Chunk file at " + x + "," + z + " is in the wrong location; relocating. " +
                    "(Expected " + x + ", " + z + ", got " + chunk.xPosition + ", " + chunk.zPosition + ")");
            rootTag.setInteger("xPos", x);
            rootTag.setInteger("zPos", z);
            chunk = ChunkLoader.loadChunkIntoWorldFromCompound(world, rootTag.getCompoundTag("Level"));
        }

        chunk.checkBlocks();
        return chunk;
    }

    @Override
    public void saveChunk(World world, Chunk chunk) throws IOException {
        world.checkSessionLock();

        try (DataOutputStream outputStream = RegionFileCache.getChunkOutputStream(this.worldDir, chunk.xPosition, chunk.zPosition)) {
            TagCompound rootTag = new TagCompound();
            TagCompound levelTag = new TagCompound();
            rootTag.setTag("Level", levelTag);
            ChunkLoader.storeChunkInCompound(chunk, world, levelTag);
            CompressedStreamTools.writeCompound(rootTag, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        WorldInfo worldInfo = world.getWorldInfo();
        worldInfo.setSizeOnDisk(worldInfo.getSizeOnDisk() + (long) RegionFileCache.getSizeDelta(this.worldDir, chunk.xPosition, chunk.zPosition));
    }

    @Override
    public void saveExtraChunkData(World world, Chunk chunk) throws IOException {
    }

    @Override
    public void onUnloadOldest() {
    }

    @Override
    public void saveExtraData() {
    }
}
