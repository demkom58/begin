package net.potion.entity;

import net.potion.material.Material;
import net.potion.util.SpawnListEntry;
import net.potion.block.BlockBed;
import net.potion.entity.ai.PathEntity;
import net.potion.entity.ai.PathPoint;
import net.potion.entity.ai.Pathfinder;
import net.potion.entity.monster.EntitySkeleton;
import net.potion.entity.monster.EntitySpider;
import net.potion.entity.monster.EntityZombie;
import net.potion.entity.passive.EntitySheep;
import net.potion.entity.player.EntityPlayer;
import net.potion.world.World;
import net.potion.world.chunk.ChunkCoordIntPair;
import net.potion.world.chunk.ChunkCoordinates;
import net.potion.world.chunk.ChunkPosition;
import net.potion.world.gen.BiomeGenBase;
import net.potion.util.MathHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SpawnerAnimals {
    protected static final Class[] field_22213_a = new Class[]{EntitySpider.class, EntityZombie.class, EntitySkeleton.class};
    private static Set<ChunkCoordIntPair> eligibleChunksForSpawning = new HashSet<>();

    protected static ChunkPosition getRandomSpawningPointInChunk(World var0, int var1, int var2) {
        int var3 = var1 + var0.rand.nextInt(16);
        int var4 = var0.rand.nextInt(128);
        int var5 = var2 + var0.rand.nextInt(16);
        return new ChunkPosition(var3, var4, var5);
    }

    public static int performSpawning(World var0, boolean var1, boolean var2) {
        if (!var1 && !var2) {
            return 0;
        } else {
            eligibleChunksForSpawning.clear();

            for (int var3 = 0; var3 < var0.playerEntities.size(); ++var3) {
                EntityPlayer var4 = var0.playerEntities.get(var3);
                int var5 = MathHelper.floor(var4.posX / 16.0D);
                int var6 = MathHelper.floor(var4.posZ / 16.0D);
                byte var7 = 8;

                for (int var8 = -var7; var8 <= var7; ++var8) {
                    for (int var9 = -var7; var9 <= var7; ++var9) {
                        eligibleChunksForSpawning.add(new ChunkCoordIntPair(var8 + var5, var9 + var6));
                    }
                }
            }

            int var35 = 0;
            ChunkCoordinates var36 = var0.getSpawnPoint();

            for (EnumCreatureType var40 : EnumCreatureType.values()) {
                if ((!var40.isPeacefulCreature() || var2) && (var40.isPeacefulCreature() || var1) && var0.countEntities(var40.getCreatureClass()) <= var40.getMaxNumberOfCreature() * eligibleChunksForSpawning.size() / 256) {
                    label130:
                    for (ChunkCoordIntPair var10 : eligibleChunksForSpawning) {
                        BiomeGenBase var11 = var0.getWorldChunkManager().getBiomeGenAtChunkCoord(var10);
                        List<SpawnListEntry> var12 = var11.getSpawnableList(var40);
                        if (var12 != null && !var12.isEmpty()) {
                            int var13 = 0;

                            for (SpawnListEntry var15 : var12) {
                                var13 += var15.spawnRarityRate;
                            }

                            int var42 = var0.rand.nextInt(var13);
                            SpawnListEntry var43 = var12.get(0);

                            for (SpawnListEntry var17 : var12) {
                                var42 -= var17.spawnRarityRate;
                                if (var42 < 0) {
                                    var43 = var17;
                                    break;
                                }
                            }

                            ChunkPosition var44 = getRandomSpawningPointInChunk(var0, var10.chunkXPos * 16, var10.chunkZPos * 16);
                            int var45 = var44.x;
                            int var18 = var44.y;
                            int var19 = var44.z;
                            if (!var0.isBlockNormalCube(var45, var18, var19) && var0.getBlockMaterial(var45, var18, var19) == var40.getCreatureMaterial()) {
                                int var20 = 0;

                                for (int var21 = 0; var21 < 3; ++var21) {
                                    int var22 = var45;
                                    int var23 = var18;
                                    int var24 = var19;
                                    byte var25 = 6;

                                    for (int var26 = 0; var26 < 4; ++var26) {
                                        var22 += var0.rand.nextInt(var25) - var0.rand.nextInt(var25);
                                        var23 += var0.rand.nextInt(1) - var0.rand.nextInt(1);
                                        var24 += var0.rand.nextInt(var25) - var0.rand.nextInt(var25);
                                        if (func_21167_a(var40, var0, var22, var23, var24)) {
                                            float var27 = (float) var22 + 0.5F;
                                            float var28 = (float) var23;
                                            float var29 = (float) var24 + 0.5F;
                                            if (var0.getClosestPlayer(var27, var28, var29, 24.0D) == null) {
                                                float var30 = var27 - (float) var36.posX;
                                                float var31 = var28 - (float) var36.posY;
                                                float var32 = var29 - (float) var36.posZ;
                                                float var33 = var30 * var30 + var31 * var31 + var32 * var32;
                                                if (var33 >= 576.0F) {
                                                    EntityLiving var46;
                                                    try {
                                                        var46 = (EntityLiving) var43.entityClass.getConstructor(World.class).newInstance(var0);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        return var35;
                                                    }

                                                    var46.setLocationAndAngles(var27, var28, var29, var0.rand.nextFloat() * 360.0F, 0.0F);
                                                    if (var46.getCanSpawnHere()) {
                                                        ++var20;
                                                        var0.entityJoinedWorld(var46);
                                                        func_21166_a(var46, var0, var27, var28, var29);
                                                        if (var20 >= var46.getMaxSpawnedInChunk()) {
                                                            continue label130;
                                                        }
                                                    }

                                                    var35 += var20;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return var35;
        }
    }

    private static boolean func_21167_a(EnumCreatureType var0, World var1, int var2, int var3, int var4) {
        if (var0.getCreatureMaterial() == Material.WATER) {
            return var1.getBlockMaterial(var2, var3, var4).isLiquid() && !var1.isBlockNormalCube(var2, var3 + 1, var4);
        } else {
            return var1.isBlockNormalCube(var2, var3 - 1, var4) && !var1.isBlockNormalCube(var2, var3, var4) && !var1.getBlockMaterial(var2, var3, var4).isLiquid() && !var1.isBlockNormalCube(var2, var3 + 1, var4);
        }
    }

    private static void func_21166_a(EntityLiving var0, World var1, float var2, float var3, float var4) {
        if (var0 instanceof EntitySpider && var1.rand.nextInt(100) == 0) {
            EntitySkeleton var5 = new EntitySkeleton(var1);
            var5.setLocationAndAngles(var2, var3, var4, var0.rotationYaw, 0.0F);
            var1.entityJoinedWorld(var5);
            var5.mountEntity(var0);
        } else if (var0 instanceof EntitySheep) {
            ((EntitySheep) var0).setFleeceColor(EntitySheep.func_21066_a(var1.rand));
        }

    }

    public static boolean performSleepSpawning(World var0, List<EntityPlayer> var1) {
        boolean var2 = false;
        Pathfinder var3 = new Pathfinder(var0);

        for (EntityPlayer var5 : var1) {
            Class[] var6 = field_22213_a;
            if (var6 != null && var6.length != 0) {
                boolean var7 = false;

                for (int var8 = 0; var8 < 20 && !var7; ++var8) {
                    int var9 = MathHelper.floor(var5.posX) + var0.rand.nextInt(32) - var0.rand.nextInt(32);
                    int var10 = MathHelper.floor(var5.posZ) + var0.rand.nextInt(32) - var0.rand.nextInt(32);
                    int var11 = MathHelper.floor(var5.posY) + var0.rand.nextInt(16) - var0.rand.nextInt(16);
                    if (var11 < 1) {
                        var11 = 1;
                    } else if (var11 > 128) {
                        var11 = 128;
                    }

                    int var12 = var0.rand.nextInt(var6.length);

                    int var13;
                    for (var13 = var11; var13 > 2 && !var0.isBlockNormalCube(var9, var13 - 1, var10); --var13) {
                    }

                    while (!func_21167_a(EnumCreatureType.MONSTER, var0, var9, var13, var10) && var13 < var11 + 16 && var13 < 128) {
                        ++var13;
                    }

                    if (var13 < var11 + 16 && var13 < 128) {
                        float var14 = (float) var9 + 0.5F;
                        float var15 = (float) var13;
                        float var16 = (float) var10 + 0.5F;

                        EntityLiving var17;
                        try {
                            var17 = (EntityLiving) var6[var12].getConstructor(World.class).newInstance(var0);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return var2;
                        }

                        var17.setLocationAndAngles(var14, var15, var16, var0.rand.nextFloat() * 360.0F, 0.0F);
                        if (var17.getCanSpawnHere()) {
                            PathEntity var18 = var3.createEntityPathTo(var17, var5, 32.0F);
                            if (var18 != null && var18.pathLength > 1) {
                                PathPoint var19 = var18.func_22211_c();
                                if (Math.abs((double) var19.xCoord - var5.posX) < 1.5D && Math.abs((double) var19.zCoord - var5.posZ) < 1.5D && Math.abs((double) var19.yCoord - var5.posY) < 1.5D) {
                                    ChunkCoordinates var20 = BlockBed.func_22021_g(var0, MathHelper.floor(var5.posX), MathHelper.floor(var5.posY), MathHelper.floor(var5.posZ), 1);
                                    if (var20 == null) {
                                        var20 = new ChunkCoordinates(var9, var13 + 1, var10);
                                    }

                                    var17.setLocationAndAngles((float) var20.posX + 0.5F, var20.posY, (float) var20.posZ + 0.5F, 0.0F, 0.0F);
                                    var0.entityJoinedWorld(var17);
                                    func_21166_a(var17, var0, (float) var20.posX + 0.5F, (float) var20.posY, (float) var20.posZ + 0.5F);
                                    var5.wakeUpPlayer(true, false, false);
                                    var17.playLivingSound();
                                    var2 = true;
                                    var7 = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return var2;
    }
}
