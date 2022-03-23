package net.potion.entity;

import net.potion.block.BlockBed;
import net.potion.entity.ai.PathEntity;
import net.potion.entity.ai.PathPoint;
import net.potion.entity.ai.Pathfinder;
import net.potion.entity.monster.EntitySkeleton;
import net.potion.entity.monster.EntitySpider;
import net.potion.entity.monster.EntityZombie;
import net.potion.entity.passive.EntitySheep;
import net.potion.entity.player.EntityPlayer;
import net.potion.material.Material;
import net.hypnosis.util.math.MathHelper;
import net.potion.util.SpawnListEntry;
import net.potion.world.World;
import net.potion.world.chunk.ChunkCoordIntPair;
import net.potion.world.chunk.ChunkCoordinates;
import net.potion.world.chunk.ChunkPosition;
import net.potion.world.gen.biome.BiomeGenBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SpawnerAnimals {
    protected static final Class[] nightSpawnEntities = new Class[]{EntitySpider.class, EntityZombie.class, EntitySkeleton.class};
    private static Set<ChunkCoordIntPair> eligibleChunksForSpawning = new HashSet<>();

    protected static ChunkPosition getRandomSpawningPointInChunk(World var0, int var1, int var2) {
        int var3 = var1 + var0.rand.nextInt(16);
        int var4 = var0.rand.nextInt(128);
        int var5 = var2 + var0.rand.nextInt(16);
        return new ChunkPosition(var3, var4, var5);
    }

    public static int performSpawning(World world, boolean var1, boolean var2) {
        if (!var1 && !var2) {
            return 0;
        }

        eligibleChunksForSpawning.clear();

        for (int i = 0; i < world.playerEntities.size(); ++i) {
            EntityPlayer player = world.playerEntities.get(i);
            int x = MathHelper.floor(player.posX / 16.0D);
            int z = MathHelper.floor(player.posZ / 16.0D);
            byte radius = 8;

            for (int sX = -radius; sX <= radius; ++sX) {
                for (int sZ = -radius; sZ <= radius; ++sZ) {
                    eligibleChunksForSpawning.add(new ChunkCoordIntPair(sX + x, sZ + z));
                }
            }
        }

        int var35 = 0;
        ChunkCoordinates spawnPoint = world.getSpawnPoint();

        for (EnumCreatureType type : EnumCreatureType.values()) {
            if ((!type.isPeacefulCreature() || var2) && (type.isPeacefulCreature() || var1)
                    && world.countEntities(type.getCreatureClass()) <= type.getMaxNumberOfCreature() * eligibleChunksForSpawning.size() / 256) {
                label130:
                for (ChunkCoordIntPair coordPair : eligibleChunksForSpawning) {
                    BiomeGenBase genBase = world.getWorldChunkManager().getBiomeGenAtChunkCoord(coordPair);
                    List<SpawnListEntry> entries = genBase.getSpawnableList(type);
                    if (entries == null || entries.isEmpty())
                        continue;

                    int var13 = 0;

                    for (SpawnListEntry spawnEntry : entries) {
                        var13 += spawnEntry.spawnRarityRate;
                    }

                    int var42 = world.rand.nextInt(var13);
                    SpawnListEntry var43 = entries.get(0);

                    for (SpawnListEntry spawnEntry : entries) {
                        var42 -= spawnEntry.spawnRarityRate;
                        if (var42 < 0) {
                            var43 = spawnEntry;
                            break;
                        }
                    }

                    ChunkPosition chunkPos = getRandomSpawningPointInChunk(world, coordPair.chunkXPos * 16, coordPair.chunkZPos * 16);
                    int x = chunkPos.x;
                    int y = chunkPos.y;
                    int z = chunkPos.z;
                    if (world.isBlockNormalCube(x, y, z) || world.getBlockMaterial(x, y, z) != type.getCreatureMaterial())
                        continue;

                    int var20 = 0;
                    for (int var21 = 0; var21 < 3; ++var21) {
                        int var22 = x;
                        int var23 = y;
                        int var24 = z;
                        byte var25 = 6;

                        for (int var26 = 0; var26 < 4; ++var26) {
                            var22 += world.rand.nextInt(var25) - world.rand.nextInt(var25);
                            var23 += world.rand.nextInt(1) - world.rand.nextInt(1);
                            var24 += world.rand.nextInt(var25) - world.rand.nextInt(var25);
                            if (!canCreatureTypeSpawnAtLocation(type, world, var22, var23, var24))
                                continue;

                            float var27 = (float) var22 + 0.5F;
                            float var28 = (float) var23;
                            float var29 = (float) var24 + 0.5F;
                            if (world.getClosestPlayer(var27, var28, var29, 24.0D) != null)
                                continue;

                            float var30 = var27 - (float) spawnPoint.x;
                            float var31 = var28 - (float) spawnPoint.y;
                            float var32 = var29 - (float) spawnPoint.z;
                            float var33 = var30 * var30 + var31 * var31 + var32 * var32;
                            EntityLiving entity;
                            if (var33 < 576.0F)
                                continue;

                            try {
                                entity = (EntityLiving) var43.entityClass.getConstructor(World.class).newInstance(world);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return var35;
                            }

                            entity.setLocationAndAngles(var27, var28, var29, world.rand.nextFloat() * 360.0F, 0.0F);
                            if (entity.getCanSpawnHere()) {
                                ++var20;
                                world.entityJoinedWorld(entity);
                                creatureSpecificInit(entity, world, var27, var28, var29);
                                if (var20 >= entity.getMaxSpawnedInChunk()) {
                                    continue label130;
                                }
                            }

                            var35 += var20;

                        }
                    }
                }
            }
        }

        return var35;
    }

    private static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType var0, World var1, int var2, int var3, int var4) {
        if (var0.getCreatureMaterial() == Material.WATER) {
            return var1.getBlockMaterial(var2, var3, var4).isLiquid() && !var1.isBlockNormalCube(var2, var3 + 1, var4);
        } else {
            return var1.isBlockNormalCube(var2, var3 - 1, var4) && !var1.isBlockNormalCube(var2, var3, var4) && !var1.getBlockMaterial(var2, var3, var4).isLiquid() && !var1.isBlockNormalCube(var2, var3 + 1, var4);
        }
    }

    private static void creatureSpecificInit(EntityLiving var0, World var1, float var2, float var3, float var4) {
        if (var0 instanceof EntitySpider && var1.rand.nextInt(100) == 0) {
            EntitySkeleton var5 = new EntitySkeleton(var1);
            var5.setLocationAndAngles(var2, var3, var4, var0.rotationYaw, 0.0F);
            var1.entityJoinedWorld(var5);
            var5.mountEntity(var0);
        } else if (var0 instanceof EntitySheep) {
            ((EntitySheep) var0).setFleeceColor(EntitySheep.getRandomFleeceColor(var1.rand));
        }

    }

    public static boolean performSleepSpawning(World var0, List<EntityPlayer> var1) {
        boolean var2 = false;
        Pathfinder var3 = new Pathfinder(var0);

        for (EntityPlayer var5 : var1) {
            Class[] var6 = nightSpawnEntities;
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
                    var13 = var11;
                    while (var13 > 2 && !var0.isBlockNormalCube(var9, var13 - 1, var10)) {
                        --var13;
                    }

                    while (!canCreatureTypeSpawnAtLocation(EnumCreatureType.MONSTER, var0, var9, var13, var10) && var13 < var11 + 16 && var13 < 128) {
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
                                PathPoint var19 = var18.getTargetPoint();
                                if (Math.abs((double) var19.xCoord - var5.posX) < 1.5D && Math.abs((double) var19.zCoord - var5.posZ) < 1.5D && Math.abs((double) var19.yCoord - var5.posY) < 1.5D) {
                                    ChunkCoordinates var20 = BlockBed.getNearestEmptyChunkCoordinates(var0, MathHelper.floor(var5.posX), MathHelper.floor(var5.posY), MathHelper.floor(var5.posZ), 1);
                                    if (var20 == null) {
                                        var20 = new ChunkCoordinates(var9, var13 + 1, var10);
                                    }

                                    var17.setLocationAndAngles((float) var20.x + 0.5F, var20.y, (float) var20.z + 0.5F, 0.0F, 0.0F);
                                    var0.entityJoinedWorld(var17);
                                    creatureSpecificInit(var17, var0, (float) var20.x + 0.5F, (float) var20.y, (float) var20.z + 0.5F);
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
