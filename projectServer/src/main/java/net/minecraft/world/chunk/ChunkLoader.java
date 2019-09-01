package net.minecraft.world.chunk;

import net.minecraft.util.CompressedStreamTools;
import net.minecraft.util.NibbleArray;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChunkLoader implements IChunkLoader {
    private File saveDir;
    private boolean createIfNecessary;

    public ChunkLoader(File saveDir, boolean createIfNecessary) {
        this.saveDir = saveDir;
        this.createIfNecessary = createIfNecessary;
    }

    public static void storeChunkInCompound(Chunk chunk, World world, NBTTagCompound chunkCompound) {
        world.checkSessionLock();
        chunkCompound.setInteger("xPos", chunk.xPosition);
        chunkCompound.setInteger("zPos", chunk.zPosition);
        chunkCompound.setLong("LastUpdate", world.getWorldTime());
        chunkCompound.setByteArray("Blocks", chunk.blocks);
        chunkCompound.setByteArray("Data", chunk.data.data);
        chunkCompound.setByteArray("SkyLight", chunk.skylightMap.data);
        chunkCompound.setByteArray("BlockLight", chunk.blocklightMap.data);
        chunkCompound.setByteArray("HeightMap", chunk.heightMap);
        chunkCompound.setBoolean("TerrainPopulated", chunk.isTerrainPopulated);
        chunk.hasEntities = false;
        NBTTagList entitiesList = new NBTTagList();

        for (int i = 0; i < chunk.entities.length; ++i) {
            for (Entity entity : chunk.entities[i]) {
                chunk.hasEntities = true;
                NBTTagCompound tagCompound = new NBTTagCompound();
                if (entity.addEntityID(tagCompound)) {
                    entitiesList.setTag(tagCompound);
                }
            }
        }

        chunkCompound.setTag("Entities", entitiesList);
        NBTTagList tileEntitiesList = new NBTTagList();

        for (TileEntity tileEntity : chunk.chunkTileEntityMap.values()) {
            NBTTagCompound var11 = new NBTTagCompound();
            tileEntity.writeToNBT(var11);
            tileEntitiesList.setTag(var11);
        }

        chunkCompound.setTag("TileEntities", tileEntitiesList);
    }

    public static Chunk loadChunkIntoWorldFromCompound(World world, NBTTagCompound chunkCompound) {
        int x = chunkCompound.getInteger("xPos");
        int z = chunkCompound.getInteger("zPos");
        Chunk chunk = new Chunk(world, x, z);
        chunk.blocks = chunkCompound.getByteArray("Blocks");
        chunk.data = new NibbleArray(chunkCompound.getByteArray("Data"));
        chunk.skylightMap = new NibbleArray(chunkCompound.getByteArray("SkyLight"));
        chunk.blocklightMap = new NibbleArray(chunkCompound.getByteArray("BlockLight"));
        chunk.heightMap = chunkCompound.getByteArray("HeightMap");
        chunk.isTerrainPopulated = chunkCompound.getBoolean("TerrainPopulated");
        if (!chunk.data.isValid()) {
            chunk.data = new NibbleArray(chunk.blocks.length);
        }

        if (chunk.heightMap == null || !chunk.skylightMap.isValid()) {
            chunk.heightMap = new byte[256];
            chunk.skylightMap = new NibbleArray(chunk.blocks.length);
            chunk.generateHeightMap();
        }

        if (!chunk.blocklightMap.isValid()) {
            chunk.blocklightMap = new NibbleArray(chunk.blocks.length);
            chunk.func_348_a();
        }

        NBTTagList entitiesList = chunkCompound.getTagList("Entities");
        if (entitiesList != null) {
            for (int i = 0; i < entitiesList.tagCount(); ++i) {
                NBTTagCompound compound = (NBTTagCompound) entitiesList.tagAt(i);
                Entity entity = EntityList.createEntityFromNBT(compound, world);
                chunk.hasEntities = true;
                if (entity != null) {
                    chunk.addEntity(entity);
                }
            }
        }

        NBTTagList tileEntitiesList = chunkCompound.getTagList("TileEntities");
        if (tileEntitiesList != null) {
            for (int i = 0; i < tileEntitiesList.tagCount(); ++i) {
                NBTTagCompound compound = (NBTTagCompound) tileEntitiesList.tagAt(i);
                TileEntity tileEntity = TileEntity.createAndLoadEntity(compound);
                if (tileEntity != null) {
                    chunk.addTileEntity(tileEntity);
                }
            }
        }

        return chunk;
    }

    private File chunkFileForXZ(int x, int z) {
        String var3 = "c." + Integer.toString(x, 36) + "." + Integer.toString(z, 36) + ".dat";
        String var4 = Integer.toString(x & 63, 36);
        String var5 = Integer.toString(z & 63, 36);
        File var6 = new File(this.saveDir, var4);
        if (!var6.exists()) {
            if (!this.createIfNecessary) {
                return null;
            }

            var6.mkdir();
        }

        var6 = new File(var6, var5);
        if (!var6.exists()) {
            if (!this.createIfNecessary) {
                return null;
            }

            var6.mkdir();
        }

        var6 = new File(var6, var3);
        return !var6.exists() && !this.createIfNecessary ? null : var6;
    }

    public Chunk loadChunk(World world, int x, int z) throws IOException {
        File file = this.chunkFileForXZ(x, z);
        if (file != null && file.exists()) {
            try {
                FileInputStream var5 = new FileInputStream(file);
                NBTTagCompound var6 = CompressedStreamTools.readGzipCompound(var5);
                if (!var6.hasKey("Level")) {
                    System.out.println("Chunk file at " + x + "," + z + " is missing level data, skipping");
                    return null;
                }

                if (!var6.getCompoundTag("Level").hasKey("Blocks")) {
                    System.out.println("Chunk file at " + x + "," + z + " is missing block data, skipping");
                    return null;
                }

                Chunk chunk = loadChunkIntoWorldFromCompound(world, var6.getCompoundTag("Level"));
                if (!chunk.isAtLocation(x, z)) {
                    System.out.println("Chunk file at " + x + "," + z + " is in the wrong location; relocating. (Expected " + x + ", " + z + ", got " + chunk.xPosition + ", " + chunk.zPosition + ")");
                    var6.setInteger("xPos", x);
                    var6.setInteger("zPos", z);
                    chunk = loadChunkIntoWorldFromCompound(world, var6.getCompoundTag("Level"));
                }

                chunk.checkBlocks();
                return chunk;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void saveChunk(World world, Chunk chunk) throws IOException {
        world.checkSessionLock();
        File fileFile = this.chunkFileForXZ(chunk.xPosition, chunk.zPosition);
        if (fileFile.exists()) {
            WorldInfo worldInfo = world.getWorldInfo();
            worldInfo.setSizeOnDisk(worldInfo.getSizeOnDisk() - fileFile.length());
        }

        try {
            File tempChunkFile = new File(this.saveDir, "tmp_chunk.dat");
            FileOutputStream outputStream = new FileOutputStream(tempChunkFile);
            NBTTagCompound var6 = new NBTTagCompound();
            NBTTagCompound var7 = new NBTTagCompound();
            var6.setTag("Level", var7);
            storeChunkInCompound(chunk, world, var7);
            CompressedStreamTools.writeGzipCompound(var6, outputStream);
            outputStream.close();
            if (fileFile.exists()) {
                fileFile.delete();
            }

            tempChunkFile.renameTo(fileFile);
            WorldInfo var8 = world.getWorldInfo();
            var8.setSizeOnDisk(var8.getSizeOnDisk() + fileFile.length());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void func_661_a() {
    }

    public void saveExtraData() {
    }

    public void saveExtraChunkData(World world, Chunk chunk) throws IOException {
    }
}
