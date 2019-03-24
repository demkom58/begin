package net.minecraft.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import util.Vec3D;

public class BlockTrapDoor extends Block {
    protected BlockTrapDoor(int var1, Material var2) {
        super(var1, var2);
        this.blockIndexInTexture = 84;
        if (var2 == Material.IRON) {
            ++this.blockIndexInTexture;
        }

        float var3 = 0.5F;
        float var4 = 1.0F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var4, 0.5F + var3);
    }

    public static boolean func_28038_d(int var0) {
        return (var0 & 4) != 0;
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
        this.func_28039_c(blockAccess.getBlockMetadata(var2, var3, var4));
    }

    public void func_28039_c(int var1) {
        float var2 = 0.1875F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var2, 1.0F);
        if (func_28038_d(var1)) {
            if ((var1 & 3) == 0) {
                this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
            }

            if ((var1 & 3) == 1) {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
            }

            if ((var1 & 3) == 2) {
                this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }

            if ((var1 & 3) == 3) {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
            }
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
            world.setBlockMetadataWithNotify(var2, var3, var4, var6 ^ 4);
            world.func_28101_a(entityPlayer, 1003, var2, var3, var4, 0);
            return true;
        }
    }

    public void func_28040_a(World var1, int var2, int var3, int var4, boolean var5) {
        int var6 = var1.getBlockMetadata(var2, var3, var4);
        boolean var7 = (var6 & 4) > 0;
        if (var7 != var5) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, var6 ^ 4);
            var1.func_28101_a(null, 1003, var2, var3, var4, 0);
        }
    }

    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        if (!world.singleplayerWorld) {
            int var6 = world.getBlockMetadata(var2, var3, var4);
            int var7 = var2;
            int var8 = var4;
            if ((var6 & 3) == 0) {
                var8 = var4 + 1;
            }

            if ((var6 & 3) == 1) {
                --var8;
            }

            if ((var6 & 3) == 2) {
                var7 = var2 + 1;
            }

            if ((var6 & 3) == 3) {
                --var7;
            }

            if (!world.isBlockNormalCube(var7, var3, var8)) {
                world.setBlockWithNotify(var2, var3, var4, 0);
                this.dropBlockAsItem(world, var2, var3, var4, var6);
            }

            if (var5 > 0 && Block.BLOCKS_LIST[var5].canProvidePower()) {
                boolean var9 = world.isBlockIndirectlyGettingPowered(var2, var3, var4);
                this.func_28040_a(world, var2, var3, var4, var9);
            }

        }
    }

    public MovingObjectPosition collisionRayTrace(World world, int var2, int var3, int var4, Vec3D var5, Vec3D var6) {
        this.setBlockBoundsBasedOnState(world, var2, var3, var4);
        return super.collisionRayTrace(world, var2, var3, var4, var5, var6);
    }

    public void onBlockPlaced(World world, int var2, int var3, int var4, int var5) {
        byte var6 = 0;
        if (var5 == 2) {
            var6 = 0;
        }

        if (var5 == 3) {
            var6 = 1;
        }

        if (var5 == 4) {
            var6 = 2;
        }

        if (var5 == 5) {
            var6 = 3;
        }

        world.setBlockMetadataWithNotify(var2, var3, var4, var6);
    }

    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int var5) {
        if (var5 == 0) {
            return false;
        } else if (var5 == 1) {
            return false;
        } else {
            if (var5 == 2) {
                ++z;
            }

            if (var5 == 3) {
                --z;
            }

            if (var5 == 4) {
                ++x;
            }

            if (var5 == 5) {
                --x;
            }

            return world.isBlockNormalCube(x, y, z);
        }
    }
}
