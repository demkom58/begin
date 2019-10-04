package net.minecraft.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkCoordinates;

import java.util.Random;

public class BlockBed extends Block {
    public static final int[][] headBlockToFootBlockMap = new int[][]{{0, 1}, {-1, 0}, {0, -1}, {1, 0}};

    public BlockBed(int var1) {
        super(var1, 134, Material.CLOTH);
        this.setBounds();
    }

    public static int getDirectionFromMetadata(int var0) {
        return var0 & 3;
    }

    public static boolean isBlockFootOfBed(int var0) {
        return (var0 & 8) != 0;
    }

    public static boolean isBedOccupied(int var0) {
        return (var0 & 4) != 0;
    }

    public static void setBedOccupied(World var0, int var1, int var2, int var3, boolean var4) {
        int var5 = var0.getBlockMetadata(var1, var2, var3);
        if (var4) {
            var5 = var5 | 4;
        } else {
            var5 = var5 & -5;
        }

        var0.setBlockMetadataWithNotify(var1, var2, var3, var5);
    }

    public static ChunkCoordinates getNearestEmptyChunkCoordinates(World world, int x, int y, int z, int var4) {
        int metadata = world.getBlockMetadata(x, y, z);
        int direction = getDirectionFromMetadata(metadata);

        for (int i = 0; i <= 1; ++i) {
            int var8 = x - headBlockToFootBlockMap[direction][0] * i - 1;
            int var9 = z - headBlockToFootBlockMap[direction][1] * i - 1;
            int var10 = var8 + 2;
            int var11 = var9 + 2;

            for (int iX = var8; iX <= var10; ++iX) {
                for (int iZ = var9; iZ <= var11; ++iZ) {
                    if (world.isBlockNormalCube(iX, y - 1, iZ) && world.isAirBlock(iX, y, iZ) && world.isAirBlock(iX, y + 1, iZ)) {
                        if (var4 <= 0)
                            return new ChunkCoordinates(iX, y, iZ);
                        --var4;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (world.multiplayerWorld)
            return true;

        int metadata = world.getBlockMetadata(x, y, z);
        if (!isBlockFootOfBed(metadata)) {
            int direction = getDirectionFromMetadata(metadata);
            x += headBlockToFootBlockMap[direction][0];
            z += headBlockToFootBlockMap[direction][1];
            if (world.getBlockId(x, y, z) != this.blockID) {
                return true;
            }

            metadata = world.getBlockMetadata(x, y, z);
        }

        if (!world.worldProvider.canRespawnHere()) {
            double var18 = (double) x + 0.5D;
            double var20 = (double) y + 0.5D;
            double var11 = (double) z + 0.5D;
            world.setBlockWithNotify(x, y, z, 0);
            int var13 = getDirectionFromMetadata(metadata);
            x = x + headBlockToFootBlockMap[var13][0];
            z = z + headBlockToFootBlockMap[var13][1];
            if (world.getBlockId(x, y, z) == this.blockID) {
                world.setBlockWithNotify(x, y, z, 0);
                var18 = (var18 + (double) x + 0.5D) / 2.0D;
                var20 = (var20 + (double) y + 0.5D) / 2.0D;
                var11 = (var11 + (double) z + 0.5D) / 2.0D;
            }

            world.newExplosion(null, (float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, 5.0F, true);
            return true;
        }

        if (isBedOccupied(metadata)) {
            EntityPlayer bedOccupier = null;

            for (EntityPlayer entityPlayer : world.playerEntities) {
                if (!entityPlayer.isPlayerSleeping())
                    continue;

                ChunkCoordinates bedCoord = entityPlayer.bedChunkCoordinates;
                if (bedCoord.x == x && bedCoord.y == y && bedCoord.z == z)
                    bedOccupier = entityPlayer;
            }

            if (bedOccupier != null) {
                player.addChatMessage("tile.bed.occupied");
                return true;
            }

            setBedOccupied(world, x, y, z, false);
        }

        EnumBedStatus status = player.sleepInBedAt(x, y, z);
        if (status == EnumBedStatus.OK) {
            setBedOccupied(world, x, y, z, true);
            return true;
        }

        if (status == EnumBedStatus.NOT_POSSIBLE_NOW)
            player.addChatMessage("tile.bed.noSleep");
        return true;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        if (var1 == 0) {
            return Block.PLANKS.blockIndexInTexture;
        } else {
            int var3 = getDirectionFromMetadata(var2);
            int var4 = ModelBed.bedDirection[var3][var1];
            if (isBlockFootOfBed(var2)) {
                if (var4 == 2) {
                    return this.blockIndexInTexture + 2 + 16;
                } else {
                    return var4 != 5 && var4 != 4 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture + 1 + 16;
                }
            } else if (var4 == 3) {
                return this.blockIndexInTexture - 1 + 16;
            } else {
                return var4 != 5 && var4 != 4 ? this.blockIndexInTexture : this.blockIndexInTexture + 16;
            }
        }
    }

    @Override
    public int getRenderType() {
        return 14;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
        this.setBounds();
    }

    @Override
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
        int var6 = var1.getBlockMetadata(var2, var3, var4);
        int var7 = getDirectionFromMetadata(var6);
        if (isBlockFootOfBed(var6)) {
            if (var1.getBlockId(var2 - headBlockToFootBlockMap[var7][0], var3, var4 - headBlockToFootBlockMap[var7][1]) != this.blockID) {
                var1.setBlockWithNotify(var2, var3, var4, 0);
            }
        } else if (var1.getBlockId(var2 + headBlockToFootBlockMap[var7][0], var3, var4 + headBlockToFootBlockMap[var7][1]) != this.blockID) {
            var1.setBlockWithNotify(var2, var3, var4, 0);
            if (!var1.multiplayerWorld) {
                this.dropBlockAsItem(var1, var2, var3, var4, var6);
            }
        }

    }

    @Override
    public int idDropped(int var1, Random var2) {
        return isBlockFootOfBed(var1) ? 0 : Item.BED.shiftedIndex;
    }

    private void setBounds() {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5625F, 1.0F);
    }

    @Override
    public void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, int var5, float var6) {
        if (!isBlockFootOfBed(var5)) {
            super.dropBlockAsItemWithChance(var1, var2, var3, var4, var5, var6);
        }

    }

    @Override
    public int getMobilityFlag() {
        return 1;
    }
}
