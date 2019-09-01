package net.minecraft.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.TagCompound;

import java.util.List;

public class WorldInfo {
    private long randomSeed;
    private int spawnX;
    private int spawnY;
    private int spawnZ;
    private long worldTime;
    private long lastTimePlayed;
    private long sizeOnDisk;
    private TagCompound playerTag;
    private int dimension;
    private String levelName;
    private int saveVersion;
    private boolean raining;
    private int rainTime;
    private boolean thundering;
    private int thunderTime;

    public WorldInfo(TagCompound compound) {
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

    public TagCompound getNBTTagCompound() {
        TagCompound compound = new TagCompound();
        this.updateTagCompound(compound, this.playerTag);
        return compound;
    }

    public TagCompound getNBTTagCompoundWithPlayer(List<EntityPlayer> entityPlayers) {
        TagCompound var2 = new TagCompound();
        EntityPlayer entityPlayer = null;
        TagCompound var4 = null;

        if (entityPlayers.size() > 0) {
            entityPlayer = entityPlayers.get(0);
        }

        if (entityPlayer != null) {
            var4 = new TagCompound();
            entityPlayer.writeToNBT(var4);
        }

        this.updateTagCompound(var2, var4);
        return var2;
    }

    private void updateTagCompound(TagCompound compound, TagCompound playerCompound) {
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

    public void setSpawnX(int spawnX) {
        this.spawnX = spawnX;
    }

    public int getSpawnY() {
        return this.spawnY;
    }

    public void setSpawnY(int spawnY) {
        this.spawnY = spawnY;
    }

    public int getSpawnZ() {
        return this.spawnZ;
    }

    public void setSpawnZ(int spawnZ) {
        this.spawnZ = spawnZ;
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

    public TagCompound getPlayerNBTTagCompound() {
        return this.playerTag;
    }

    public void setPlayerNBTTagCompound(TagCompound playerTag) {
        this.playerTag = playerTag;
    }

    public int getDimension() {
        return this.dimension;
    }

    public void setSpawn(int spawnX, int spawnY, int spawnZ) {
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnZ = spawnZ;
    }

    public String getWorldName() {
        return this.levelName;
    }

    public void setWorldName(String levelName) {
        this.levelName = levelName;
    }

    public int getSaveVersion() {
        return this.saveVersion;
    }

    public void setSaveVersion(int saveVersion) {
        this.saveVersion = saveVersion;
    }

    public long getLastTimePlayed() {
        return this.lastTimePlayed;
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
}
