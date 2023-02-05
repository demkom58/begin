package net.minecraft.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.hypnosis.util.math.Vec3d;

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

    public static boolean isTrapdoorOpen(int var0) {
        return (var0 & 4) != 0;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getRenderType() {
        return 0;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        this.setBlockBoundsBasedOnState(var1, var2, var3, var4);
        return super.getCollisionBoundingBoxFromPool(var1, var2, var3, var4);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        this.setBlockBoundsForBlockRender(blockAccess.getBlockMetadata(x, y, z));
    }

    @Override
    @Side(CodeSide.CLIENT)
    public void setBlockBoundsForItemRender() {
        float var1 = 0.1875F;
        this.setBlockBounds(0.0F, 0.5F - var1 / 2.0F, 0.0F, 1.0F, 0.5F + var1 / 2.0F, 1.0F);
    }

    public void setBlockBoundsForBlockRender(int var1) {
        float var2 = 0.1875F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var2, 1.0F);
        if (isTrapdoorOpen(var1)) {
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

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        this.blockActivated(world, x, y, z, player);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (this.blockMaterial == Material.IRON) {
            return true;
        }

        int metadata = world.getBlockMetadata(x, y, z);
        world.setBlockMetadataWithNotify(x, y, z, metadata ^ 4);
        world.playEffects(player, 1003, x, y, z, 0);
        return true;
    }

    public void onPoweredBlockChange(World var1, int var2, int var3, int var4, boolean var5) {
        int var6 = var1.getBlockMetadata(var2, var3, var4);
        boolean var7 = (var6 & 4) > 0;
        if (var7 != var5) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, var6 ^ 4);
            var1.playEffects(null, 1003, var2, var3, var4, 0);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int var5) {
        if (!world.localWorld) {
            int var6 = world.getBlockMetadata(x, y, z);
            int var7 = x;
            int var8 = z;
            if ((var6 & 3) == 0) {
                var8 = z + 1;
            }

            if ((var6 & 3) == 1) {
                --var8;
            }

            if ((var6 & 3) == 2) {
                var7 = x + 1;
            }

            if ((var6 & 3) == 3) {
                --var7;
            }

            if (!world.isBlockNormalCube(var7, y, var8)) {
                world.setBlockWithNotify(x, y, z, 0);
                this.dropBlockAsItem(world, x, y, z, var6);
            }

            if (var5 > 0 && Block.BLOCKS_LIST[var5].canProvidePower()) {
                boolean var9 = world.isBlockIndirectlyGettingPowered(x, y, z);
                this.onPoweredBlockChange(world, x, y, z, var9);
            }

        }
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3d var5, Vec3d var6) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.collisionRayTrace(world, x, y, z, var5, var6);
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, int side) {
        byte var6 = 0;
        if (side == 2) {
            var6 = 0;
        }

        if (side == 3) {
            var6 = 1;
        }

        if (side == 4) {
            var6 = 2;
        }

        if (side == 5) {
            var6 = 3;
        }

        world.setBlockMetadataWithNotify(x, y, z, var6);
    }

    @Override
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
