package net.potion.block;

import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.util.MovingObjectPosition;
import net.potion.world.IBlockAccess;
import net.potion.world.World;
import net.potion.util.Vec3D;

import java.util.Random;

public class BlockRail extends Block {
    private final boolean field_27034_a;

    protected BlockRail(int var1, int var2, boolean var3) {
        super(var1, var2, Material.CIRCUITS);
        this.field_27034_a = var3;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    public static boolean func_27029_g(World var0, int var1, int var2, int var3) {
        int var4 = var0.getBlockId(var1, var2, var3);
        return var4 == Block.RAIL.blockID || var4 == Block.RAIL_POWERED.blockID || var4 == Block.RAIL_DETECTOR.blockID;
    }

    public static boolean func_27030_c(int var0) {
        return var0 == Block.RAIL.blockID || var0 == Block.RAIL_POWERED.blockID || var0 == Block.RAIL_DETECTOR.blockID;
    }

    // $FF: synthetic method
    public static boolean func_27033_a(BlockRail var0) {
        return var0.field_27034_a;
    }

    public boolean func_27028_d() {
        return this.field_27034_a;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int var2, int var3, int var4, Vec3D var5, Vec3D var6) {
        this.setBlockBoundsBasedOnState(world, var2, var3, var4);
        return super.collisionRayTrace(world, var2, var3, var4, var5, var6);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int var2, int var3, int var4) {
        int var5 = blockAccess.getBlockMetadata(var2, var3, var4);
        if (var5 >= 2 && var5 <= 5) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        } else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        }

    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        if (this.field_27034_a) {
            if (this.blockID == Block.RAIL_POWERED.blockID && (var2 & 8) == 0) {
                return this.blockIndexInTexture - 16;
            }
        } else if (var2 >= 6) {
            return this.blockIndexInTexture - 16;
        }

        return this.blockIndexInTexture;
    }

    @Override
    public boolean isACube() {
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        return world.isBlockNormalCube(var2, var3 - 1, var4);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (!world.singleplayerWorld) {
            this.func_4038_g(world, x, y, z, true);
        }

    }

    @Override
    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        if (!world.singleplayerWorld) {
            int var6 = world.getBlockMetadata(var2, var3, var4);
            int var7 = var6;
            if (this.field_27034_a) {
                var7 = var6 & 7;
            }

            boolean var8 = false;
            if (!world.isBlockNormalCube(var2, var3 - 1, var4)) {
                var8 = true;
            }

            if (var7 == 2 && !world.isBlockNormalCube(var2 + 1, var3, var4)) {
                var8 = true;
            }

            if (var7 == 3 && !world.isBlockNormalCube(var2 - 1, var3, var4)) {
                var8 = true;
            }

            if (var7 == 4 && !world.isBlockNormalCube(var2, var3, var4 - 1)) {
                var8 = true;
            }

            if (var7 == 5 && !world.isBlockNormalCube(var2, var3, var4 + 1)) {
                var8 = true;
            }

            if (var8) {
                this.dropBlockAsItem(world, var2, var3, var4, world.getBlockMetadata(var2, var3, var4));
                world.setBlockWithNotify(var2, var3, var4, 0);
            } else if (this.blockID == Block.RAIL_POWERED.blockID) {
                boolean var9 = world.isBlockIndirectlyGettingPowered(var2, var3, var4) || world.isBlockIndirectlyGettingPowered(var2, var3 + 1, var4);
                var9 = var9 || this.func_27032_a(world, var2, var3, var4, var6, true, 0) || this.func_27032_a(world, var2, var3, var4, var6, false, 0);
                boolean var10 = false;
                if (var9 && (var6 & 8) == 0) {
                    world.setBlockMetadataWithNotify(var2, var3, var4, var7 | 8);
                    var10 = true;
                } else if (!var9 && (var6 & 8) != 0) {
                    world.setBlockMetadataWithNotify(var2, var3, var4, var7);
                    var10 = true;
                }

                if (var10) {
                    world.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
                    if (var7 == 2 || var7 == 3 || var7 == 4 || var7 == 5) {
                        world.notifyBlocksOfNeighborChange(var2, var3 + 1, var4, this.blockID);
                    }
                }
            } else if (var5 > 0 && Block.BLOCKS_LIST[var5].canProvidePower() && !this.field_27034_a && RailLogic.getNAdjacentTracks(new RailLogic(this, world, var2, var3, var4)) == 3) {
                this.func_4038_g(world, var2, var3, var4, false);
            }

        }
    }

    private void func_4038_g(World var1, int var2, int var3, int var4, boolean var5) {
        if (!var1.singleplayerWorld) {
            new RailLogic(this, var1, var2, var3, var4).func_596_a(var1.isBlockIndirectlyGettingPowered(var2, var3, var4), var5);
        }
    }

    private boolean func_27032_a(World var1, int var2, int var3, int var4, int var5, boolean var6, int var7) {
        if (var7 >= 8) {
            return false;
        } else {
            int var8 = var5 & 7;
            boolean var9 = true;
            switch (var8) {
                case 0:
                    if (var6) {
                        ++var4;
                    } else {
                        --var4;
                    }
                    break;
                case 1:
                    if (var6) {
                        --var2;
                    } else {
                        ++var2;
                    }
                    break;
                case 2:
                    if (var6) {
                        --var2;
                    } else {
                        ++var2;
                        ++var3;
                        var9 = false;
                    }

                    var8 = 1;
                    break;
                case 3:
                    if (var6) {
                        --var2;
                        ++var3;
                        var9 = false;
                    } else {
                        ++var2;
                    }

                    var8 = 1;
                    break;
                case 4:
                    if (var6) {
                        ++var4;
                    } else {
                        --var4;
                        ++var3;
                        var9 = false;
                    }

                    var8 = 0;
                    break;
                case 5:
                    if (var6) {
                        ++var4;
                        ++var3;
                        var9 = false;
                    } else {
                        --var4;
                    }

                    var8 = 0;
            }

            if (this.func_27031_a(var1, var2, var3, var4, var6, var7, var8)) {
                return true;
            } else {
                return var9 && this.func_27031_a(var1, var2, var3 - 1, var4, var6, var7, var8);
            }
        }
    }

    private boolean func_27031_a(World var1, int var2, int var3, int var4, boolean var5, int var6, int var7) {
        int var8 = var1.getBlockId(var2, var3, var4);
        if (var8 == Block.RAIL_POWERED.blockID) {
            int var9 = var1.getBlockMetadata(var2, var3, var4);
            int var10 = var9 & 7;
            if (var7 == 1 && (var10 == 0 || var10 == 4 || var10 == 5)) {
                return false;
            }

            if (var7 == 0 && (var10 == 1 || var10 == 2 || var10 == 3)) {
                return false;
            }

            if ((var9 & 8) != 0) {
                if (!var1.isBlockIndirectlyGettingPowered(var2, var3, var4) && !var1.isBlockIndirectlyGettingPowered(var2, var3 + 1, var4)) {
                    return this.func_27032_a(var1, var2, var3, var4, var9, var5, var6 + 1);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public int getMobilityFlag() {
        return 0;
    }
}
