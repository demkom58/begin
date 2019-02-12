package net.minecraft.src;

import java.util.List;

public class WorldInfo {
   private long randomSeed;
   private int spawnX;
   private int spawnY;
   private int spawnZ;
   private long worldTime;
   private long lastTimePlayed;
   private long sizeOnDisk;
   private NBTTagCompound field_22195_h;
   private int field_22194_i;
   private String levelName;
   private int saveVersion;
   private boolean isRaining;
   private int rainTime;
   private boolean isThundering;
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
      this.isRaining = compound.getBoolean("raining");
      this.thunderTime = compound.getInteger("thunderTime");
      this.isThundering = compound.getBoolean("thundering");
      if (compound.hasKey("Player")) {
         this.field_22195_h = compound.getCompoundTag("Player");
         this.field_22194_i = this.field_22195_h.getInteger("Dimension");
      }

   }

   public WorldInfo(long var1, String var3) {
      this.randomSeed = var1;
      this.levelName = var3;
   }

   public WorldInfo(WorldInfo info) {
      this.randomSeed = info.randomSeed;
      this.spawnX = info.spawnX;
      this.spawnY = info.spawnY;
      this.spawnZ = info.spawnZ;
      this.worldTime = info.worldTime;
      this.lastTimePlayed = info.lastTimePlayed;
      this.sizeOnDisk = info.sizeOnDisk;
      this.field_22195_h = info.field_22195_h;
      this.field_22194_i = info.field_22194_i;
      this.levelName = info.levelName;
      this.saveVersion = info.saveVersion;
      this.rainTime = info.rainTime;
      this.isRaining = info.isRaining;
      this.thunderTime = info.thunderTime;
      this.isThundering = info.isThundering;
   }

   public NBTTagCompound func_22185_a() {
      NBTTagCompound var1 = new NBTTagCompound();
      this.saveNBTTag(var1, this.field_22195_h);
      return var1;
   }

   public NBTTagCompound func_22183_a(List<EntityPlayer> var1) {
      NBTTagCompound var2 = new NBTTagCompound();
      EntityPlayer var3 = null;
      NBTTagCompound var4 = null;
      if (var1.size() > 0) {
         var3 = var1.get(0);
      }

      if (var3 != null) {
         var4 = new NBTTagCompound();
         var3.writeToNBT(var4);
      }

      this.saveNBTTag(var2, var4);
      return var2;
   }

   private void saveNBTTag(NBTTagCompound var1, NBTTagCompound var2) {
      var1.setLong("RandomSeed", this.randomSeed);
      var1.setInteger("SpawnX", this.spawnX);
      var1.setInteger("SpawnY", this.spawnY);
      var1.setInteger("SpawnZ", this.spawnZ);
      var1.setLong("Time", this.worldTime);
      var1.setLong("SizeOnDisk", this.sizeOnDisk);
      var1.setLong("LastPlayed", System.currentTimeMillis());
      var1.setString("LevelName", this.levelName);
      var1.setInteger("version", this.saveVersion);
      var1.setInteger("rainTime", this.rainTime);
      var1.setBoolean("raining", this.isRaining);
      var1.setInteger("thunderTime", this.thunderTime);
      var1.setBoolean("thundering", this.isThundering);
      if (var2 != null) {
         var1.setCompoundTag("Player", var2);
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

   public long getSizeOnDisk() {
      return this.sizeOnDisk;
   }

   public int getDimension() {
      return this.field_22194_i;
   }

   public void setWorldTime(long var1) {
      this.worldTime = var1;
   }

   public void setSizeOnDisk(long var1) {
      this.sizeOnDisk = var1;
   }

   public void setSpawnPosition(int var1, int var2, int var3) {
      this.spawnX = var1;
      this.spawnY = var2;
      this.spawnZ = var3;
   }

   public void setLevelName(String var1) {
      this.levelName = var1;
   }

   public int getVersion() {
      return this.saveVersion;
   }

   public void setVersion(int var1) {
      this.saveVersion = var1;
   }

   public boolean getIsThundering() {
      return this.isThundering;
   }

   public void setIsThundering(boolean var1) {
      this.isThundering = var1;
   }

   public int getThunderTime() {
      return this.thunderTime;
   }

   public void setThunderTime(int var1) {
      this.thunderTime = var1;
   }

   public boolean getIsRaining() {
      return this.isRaining;
   }

   public void setIsRaining(boolean var1) {
      this.isRaining = var1;
   }

   public int getRainTime() {
      return this.rainTime;
   }

   public void setRainTime(int var1) {
      this.rainTime = var1;
   }
}
