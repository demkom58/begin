package net.minecraft;

import util.Vec3D;

import java.util.Random;

public class BlockDoor extends Block {
    protected BlockDoor(int var1, Material var2) {
        super(var1, var2);
        this.blockIndexInTexture = 97;
        if (var2 == Material.IRON) {
            ++this.blockIndexInTexture;
        }

        float var3 = 0.5F;
        float var4 = 1.0F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var4, 0.5F + var3);
    }

    public static boolean func_27036_e(int var0) {
        return (var0 & 4) != 0;
    }

    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        if (var1 != 0 && var1 != 1) {
            int var3 = this.func_271_d(var2);
            if ((var3 == 0 || var3 == 2) ^ var1 <= 3) {
                return this.blockIndexInTexture;
            } else {
                int var4 = var3 / 2 + (var1 & 1 ^ var3);
                var4 = var4 + (var2 & 4) / 4;
                int var5 = this.blockIndexInTexture - (var2 & 8) * 2;
                if ((var4 & 1) != 0) {
                    var5 = -var5;
                }

                return var5;
            }
        } else {
            return this.blockIndexInTexture;
        }
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isACube() {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int var2, int var3, int var4) {
        this.func_273_b(this.func_271_d(blockAccess.getBlockMetadata(var2, var3, var4)));
    }

    public void func_273_b(int var1) {
        float var2 = 0.1875F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
        if (var1 == 0) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
        }

        if (var1 == 1) {
            this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if (var1 == 2) {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
        }

        if (var1 == 3) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
        }

    }

    public void onBlockClicked(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        this.blockActivated(world, var2, var3, var4, entityPlayer);
    }

    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        if (this.blockMaterial == Material.IRON) {
            return true;
        } else {
            int var6 = world.getBlockMetadata(var2, var3, var4);
            if ((var6 & 8) != 0) {
                if (world.getBlockId(var2, var3 - 1, var4) == this.blockID) {
                    this.blockActivated(world, var2, var3 - 1, var4, entityPlayer);
                }

                return true;
            } else {
                if (world.getBlockId(var2, var3 + 1, var4) == this.blockID) {
                    world.setBlockMetadataWithNotify(var2, var3 + 1, var4, (var6 ^ 4) + 8);
                }

                world.setBlockMetadataWithNotify(var2, var3, var4, var6 ^ 4);
                world.markBlocksDirty(var2, var3 - 1, var4, var2, var3, var4);
                world.func_28101_a(entityPlayer, 1003, var2, var3, var4, 0);
                return true;
            }
        }
    }

    public void func_272_a(World var1, int var2, int var3, int var4, boolean var5) {
        int var6 = var1.getBlockMetadata(var2, var3, var4);
        if ((var6 & 8) != 0) {
            if (var1.getBlockId(var2, var3 - 1, var4) == this.blockID) {
                this.func_272_a(var1, var2, var3 - 1, var4, var5);
            }

        } else {
            boolean var7 = (var1.getBlockMetadata(var2, var3, var4) & 4) > 0;
            if (var7 != var5) {
                if (var1.getBlockId(var2, var3 + 1, var4) == this.blockID) {
                    var1.setBlockMetadataWithNotify(var2, var3 + 1, var4, (var6 ^ 4) + 8);
                }

                var1.setBlockMetadataWithNotify(var2, var3, var4, var6 ^ 4);
                var1.markBlocksDirty(var2, var3 - 1, var4, var2, var3, var4);
                var1.func_28101_a(null, 1003, var2, var3, var4, 0);
            }
        }
    }

    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        int var6 = world.getBlockMetadata(var2, var3, var4);
        if ((var6 & 8) != 0) {
            if (world.getBlockId(var2, var3 - 1, var4) != this.blockID) {
                world.setBlockWithNotify(var2, var3, var4, 0);
            }

            if (var5 > 0 && Block.BLOCKS_LIST[var5].canProvidePower()) {
                this.onNeighborBlockChange(world, var2, var3 - 1, var4, var5);
            }
        } else {
            boolean var7 = false;
            if (world.getBlockId(var2, var3 + 1, var4) != this.blockID) {
                world.setBlockWithNotify(var2, var3, var4, 0);
                var7 = true;
            }

            if (!world.isBlockNormalCube(var2, var3 - 1, var4)) {
                world.setBlockWithNotify(var2, var3, var4, 0);
                var7 = true;
                if (world.getBlockId(var2, var3 + 1, var4) == this.blockID) {
                    world.setBlockWithNotify(var2, var3 + 1, var4, 0);
                }
            }

            if (var7) {
                if (!world.singleplayerWorld) {
                    this.dropBlockAsItem(world, var2, var3, var4, var6);
                }
            } else if (var5 > 0 && Block.BLOCKS_LIST[var5].canProvidePower()) {
                boolean var8 = world.isBlockIndirectlyGettingPowered(var2, var3, var4) || world.isBlockIndirectlyGettingPowered(var2, var3 + 1, var4);
                this.func_272_a(world, var2, var3, var4, var8);
            }
        }

    }

    public int idDropped(int var1, Random random) {
        if ((var1 & 8) != 0) {
            return 0;
        } else {
            return this.blockMaterial == Material.IRON ? Item.DOOR_IRON.shiftedIndex : Item.DOOR_WOOD.shiftedIndex;
        }
    }

    public MovingObjectPosition collisionRayTrace(World world, int var2, int var3, int var4, Vec3D var5, Vec3D var6) {
        this.setBlockBoundsBasedOnState(world, var2, var3, var4);
        return super.collisionRayTrace(world, var2, var3, var4, var5, var6);
    }

    public int func_271_d(int var1) {
        return (var1 & 4) == 0 ? var1 - 1 & 3 : var1 & 3;
    }

    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        if (var3 >= 127) {
            return false;
        } else {
            return world.isBlockNormalCube(var2, var3 - 1, var4) && super.canPlaceBlockAt(world, var2, var3, var4) && super.canPlaceBlockAt(world, var2, var3 + 1, var4);
        }
    }

    public int getMobilityFlag() {
        return 1;
    }
}
