package net.minecraft;

import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class WorldInfo {
    private long randomSeed;
    private int spawnX;
    private int spawnY;
    private int spawnZ;
    private long worldTime;
    private long lastTimePlayed;
    private long sizeOnDisk;
    private NBTTagCompound playerTag;
    private int dimension;
    private String levelName;
    private int saveVersion;
    private boolean raining;
    private int rainTime;
    private boolean thundering;
    private int thunderTime;

    public WorldInfo(NBTTagCompound compound) {
        this.randomSeed = compound.getLong("RandomSeed");
        this.spawnX = compound.getInteger("SpawnX");
        this.spawnY = compound.getInteger("SpawnY");
        this.spawnZ = compound.getInteger("SpawnZ");
        this.worldTime = compound.getLong("Time");
        this.lastTimePlayed = compound.getLong("LastPlayed");
        this.sizeOnDisk = compound.getLong("SizeOnDisk");
        this.levelName = compound.getString("LevelName");
        this.saveVersion = compound.getInteger("version");
        this.rainTime = compound.getInteger("rainTime");
        this.raining = compound.getBoolean("raining");
        this.thunderTime = compound.getInteger("thunderTime");
        this.thundering = compound.getBoolean("thundering");
        if (compound.hasKey("Player")) {
            this.playerTag = compound.getCompoundTag("Player");
            this.dimension = this.playerTag.getInteger("Dimension");
        }

    }

    public WorldInfo(long randomSeed, String levelName) {
        this.randomSeed = randomSeed;
        this.levelName = levelName;
    }

    public WorldInfo(WorldInfo info) {
        this.randomSeed = info.randomSeed;
        this.spawnX = info.spawnX;
        this.spawnY = info.spawnY;
        this.spawnZ = info.spawnZ;
        this.worldTime = info.worldTime;
        this.lastTimePlayed = info.lastTimePlayed;
        this.sizeOnDisk = info.sizeOnDisk;
        this.playerTag = info.playerTag;
        this.dimension = info.dimension;
        this.levelName = info.levelName;
        this.saveVersion = info.saveVersion;
        this.rainTime = info.rainTime;
        this.raining = info.raining;
        this.thunderTime = info.thunderTime;
        this.thundering = info.thundering;
    }

    public NBTTagCompound getNBTTagCompound() {
        NBTTagCompound compound = new NBTTagCompound();
        this.saveNBTTag(compound, this.playerTag);
        return compound;
    }

    public NBTTagCompound getNBTTagCompoundWithPlayer(List<EntityPlayer> entityPlayers) {
        NBTTagCompound var2 = new NBTTagCompound();
        EntityPlayer entityPlayer = null;
        NBTTagCompound var4 = null;

        if (entityPlayers.size() > 0) {
            entityPlayer = entityPlayers.get(0);
        }

        if (entityPlayer != null) {
            var4 = new NBTTagCompound();
            entityPlayer.writeToNBT(var4);
        }

        this.saveNBTTag(var2, var4);
        return var2;
    }

    private void saveNBTTag(NBTTagCompound compound, NBTTagCompound playerCompound) {
        compound.setLong("RandomSeed", this.randomSeed);
        compound.setInteger("SpawnX", this.spawnX);
        compound.setInteger("SpawnY", this.spawnY);
        compound.setInteger("SpawnZ", this.spawnZ);
        compound.setLong("Time", this.worldTime);
        compound.setLong("SizeOnDisk", this.sizeOnDisk);
        compound.setLong("LastPlayed", System.currentTimeMillis());
        compound.setString("LevelName", this.levelName);
        compound.setInteger("version", this.saveVersion);
        compound.setInteger("rainTime", this.rainTime);
        compound.setBoolean("raining", this.raining);
        compound.setInteger("thunderTime", this.thunderTime);
        compound.setBoolean("thundering", this.thundering);
        if (playerCompound != null) {
            compound.setCompoundTag("Player", playerCompound);
        }

    }

    public long getRandomSeed() {
        return this.randomSeed;
    }

    public int getSpawnX() {
        return this.spawnX;
    }

    public int getSpawnY() {
        return this.spawnY;
    }

    public int getSpawnZ() {
        return this.spawnZ;
    }

    public long getWorldTime() {
        return this.worldTime;
    }

    public void setWorldTime(long worldTime) {
        this.worldTime = worldTime;
    }

    public long getSizeOnDisk() {
        return this.sizeOnDisk;
    }

    public void setSizeOnDisk(long sizeOnDisk) {
        this.sizeOnDisk = sizeOnDisk;
    }

    public int getDimension() {
        return this.dimension;
    }

    public void setSpawn(int spawnX, int spawnY, int spawnZ) {
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnZ = spawnZ;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getVersion() {
        return this.saveVersion;
    }

    public void setVersion(int saveVersion) {
        this.saveVersion = saveVersion;
    }

    public boolean isThundering() {
        return this.thundering;
    }

    public void setThundering(boolean thundering) {
        this.thundering = thundering;
    }

    public int getThunderTime() {
        return this.thunderTime;
    }

    public void setThunderTime(int thunderTime) {
        this.thunderTime = thunderTime;
    }

    public boolean isRaining() {
        return this.raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public int getRainTime() {
        return this.rainTime;
    }

    public void setRainTime(int rainTime) {
        this.rainTime = rainTime;
    }

    public String getLevelName() {
        return levelName;
    }

    public int getSaveVersion() {
        return saveVersion;
    }

    public long getLastTimePlayed() {
        return lastTimePlayed;
    }

    public NBTTagCompound getPlayerTag() {
        return playerTag;
    }

}
