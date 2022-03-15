package net.potion.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.item.Item;
import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.world.IBlockAccess;
import net.potion.world.World;
import net.potion.world.chunk.ChunkPosition;

import java.util.*;

public class BlockRedstoneWire extends Block {
    private boolean wiresProvidePower = true;
    private final Set<ChunkPosition> chunkPositions = new HashSet<>();

    public BlockRedstoneWire(int var1, int var2) {
        super(var1, var2, Material.CIRCUITS);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    public static boolean isPowerProviderOrWire(IBlockAccess var0, int var1, int var2, int var3, int var4) {
        int var5 = var0.getBlockId(var1, var2, var3);
        if (var5 == Block.REDSTONE_WIRE.blockID) {
            return true;
        } else if (var5 == 0) {
            return false;
        } else if (Block.BLOCKS_LIST[var5].canProvidePower()) {
            return true;
        } else if (var5 != Block.REDSTONE_REPEATER_IDLE.blockID && var5 != Block.REDSTONE_REPEATER_ACTIVE.blockID) {
            return false;
        } else {
            int var6 = var0.getBlockMetadata(var1, var2, var3);
            return var4 == ModelBed.field2[var6 & 3];
        }
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        return this.blockIndexInTexture;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Side(CodeSide.CLIENT)
    @Override
    public int getRenderType() {
        return 5;
    }

    @Side(CodeSide.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
        return 8388608;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.isBlockNormalCube(x, y - 1, z);
    }

    private void updateAndPropagateCurrentStrength(World var1, int var2, int var3, int var4) {
        this.method1(var1, var2, var3, var4, var2, var3, var4);
        List<ChunkPosition> var5 = new ArrayList<>(this.chunkPositions);
        this.chunkPositions.clear();

        for (int var6 = 0; var6 < var5.size(); ++var6) {
            ChunkPosition var7 = var5.get(var6);
            var1.notifyBlocksOfNeighborChange(var7.x, var7.y, var7.z, this.blockID);
        }

    }

    private void method1(World var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        int var8 = var1.getBlockMetadata(var2, var3, var4);
        int var9 = 0;
        this.wiresProvidePower = false;
        boolean var10 = var1.isBlockIndirectlyGettingPowered(var2, var3, var4);
        this.wiresProvidePower = true;
        if (var10) {
            var9 = 15;
        } else {
            for (int var11 = 0; var11 < 4; ++var11) {
                int var12 = var2;
                int var13 = var4;
                if (var11 == 0) {
                    var12 = var2 - 1;
                }

                if (var11 == 1) {
                    ++var12;
                }

                if (var11 == 2) {
                    var13 = var4 - 1;
                }

                if (var11 == 3) {
                    ++var13;
                }

                if (var12 != var5 || var3 != var6 || var13 != var7) {
                    var9 = this.getMaxCurrentStrength(var1, var12, var3, var13, var9);
                }

                if (var1.isBlockNormalCube(var12, var3, var13) && !var1.isBlockNormalCube(var2, var3 + 1, var4)) {
                    if (var12 != var5 || var3 + 1 != var6 || var13 != var7) {
                        var9 = this.getMaxCurrentStrength(var1, var12, var3 + 1, var13, var9);
                    }
                } else if (!var1.isBlockNormalCube(var12, var3, var13) && (var12 != var5 || var3 - 1 != var6 || var13 != var7)) {
                    var9 = this.getMaxCurrentStrength(var1, var12, var3 - 1, var13, var9);
                }
            }

            if (var9 > 0) {
                --var9;
            } else {
                var9 = 0;
            }
        }

        if (var8 != var9) {
            var1.editingBlocks = true;
            var1.setBlockMetadataWithNotify(var2, var3, var4, var9);
            var1.markBlocksDirty(var2, var3, var4, var2, var3, var4);
            var1.editingBlocks = false;

            for (int var18 = 0; var18 < 4; ++var18) {
                int var19 = var2;
                int var20 = var4;
                int var14 = var3 - 1;
                if (var18 == 0) {
                    var19 = var2 - 1;
                }

                if (var18 == 1) {
                    ++var19;
                }

                if (var18 == 2) {
                    var20 = var4 - 1;
                }

                if (var18 == 3) {
                    ++var20;
                }

                if (var1.isBlockNormalCube(var19, var3, var20)) {
                    var14 += 2;
                }

                int var15 = 0;
                var15 = this.getMaxCurrentStrength(var1, var19, var3, var20, -1);
                var9 = var1.getBlockMetadata(var2, var3, var4);
                if (var9 > 0) {
                    --var9;
                }

                if (var15 >= 0 && var15 != var9) {
                    this.method1(var1, var19, var3, var20, var2, var3, var4);
                }

                var15 = this.getMaxCurrentStrength(var1, var19, var14, var20, -1);
                var9 = var1.getBlockMetadata(var2, var3, var4);
                if (var9 > 0) {
                    --var9;
                }

                if (var15 >= 0 && var15 != var9) {
                    this.method1(var1, var19, var14, var20, var2, var3, var4);
                }
            }

            if (var8 == 0 || var9 == 0) {
                this.chunkPositions.add(new ChunkPosition(var2, var3, var4));
                this.chunkPositions.add(new ChunkPosition(var2 - 1, var3, var4));
                this.chunkPositions.add(new ChunkPosition(var2 + 1, var3, var4));
                this.chunkPositions.add(new ChunkPosition(var2, var3 - 1, var4));
                this.chunkPositions.add(new ChunkPosition(var2, var3 + 1, var4));
                this.chunkPositions.add(new ChunkPosition(var2, var3, var4 - 1));
                this.chunkPositions.add(new ChunkPosition(var2, var3, var4 + 1));
            }
        }

    }

    private void notifyWireNeighborsOfNeighborChange(World var1, int var2, int var3, int var4) {
        if (var1.getBlockId(var2, var3, var4) == this.blockID) {
            var1.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2 - 1, var3, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2 + 1, var3, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2, var3, var4 - 1, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2, var3, var4 + 1, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2, var3 + 1, var4, this.blockID);
        }
    }

    @Override
    public void onBlockAdded(World var1, int var2, int var3, int var4) {
        super.onBlockAdded(var1, var2, var3, var4);
        if (var1.localWorld) {
            return;
        }

        this.updateAndPropagateCurrentStrength(var1, var2, var3, var4);
        var1.notifyBlocksOfNeighborChange(var2, var3 + 1, var4, this.blockID);
        var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
        this.notifyWireNeighborsOfNeighborChange(var1, var2 - 1, var3, var4);
        this.notifyWireNeighborsOfNeighborChange(var1, var2 + 1, var3, var4);
        this.notifyWireNeighborsOfNeighborChange(var1, var2, var3, var4 - 1);
        this.notifyWireNeighborsOfNeighborChange(var1, var2, var3, var4 + 1);
        if (var1.isBlockNormalCube(var2 - 1, var3, var4)) {
            this.notifyWireNeighborsOfNeighborChange(var1, var2 - 1, var3 + 1, var4);
        } else {
            this.notifyWireNeighborsOfNeighborChange(var1, var2 - 1, var3 - 1, var4);
        }

        if (var1.isBlockNormalCube(var2 + 1, var3, var4)) {
            this.notifyWireNeighborsOfNeighborChange(var1, var2 + 1, var3 + 1, var4);
        } else {
            this.notifyWireNeighborsOfNeighborChange(var1, var2 + 1, var3 - 1, var4);
        }

        if (var1.isBlockNormalCube(var2, var3, var4 - 1)) {
            this.notifyWireNeighborsOfNeighborChange(var1, var2, var3 + 1, var4 - 1);
        } else {
            this.notifyWireNeighborsOfNeighborChange(var1, var2, var3 - 1, var4 - 1);
        }

        if (var1.isBlockNormalCube(var2, var3, var4 + 1)) {
            this.notifyWireNeighborsOfNeighborChange(var1, var2, var3 + 1, var4 + 1);
        } else {
            this.notifyWireNeighborsOfNeighborChange(var1, var2, var3 - 1, var4 + 1);
        }

    }

    @Override
    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        super.onBlockRemoval(var1, var2, var3, var4);
        if (var1.localWorld) {
            return;
        }

        var1.notifyBlocksOfNeighborChange(var2, var3 + 1, var4, this.blockID);
        var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
        this.updateAndPropagateCurrentStrength(var1, var2, var3, var4);
        this.notifyWireNeighborsOfNeighborChange(var1, var2 - 1, var3, var4);
        this.notifyWireNeighborsOfNeighborChange(var1, var2 + 1, var3, var4);
        this.notifyWireNeighborsOfNeighborChange(var1, var2, var3, var4 - 1);
        this.notifyWireNeighborsOfNeighborChange(var1, var2, var3, var4 + 1);
        if (var1.isBlockNormalCube(var2 - 1, var3, var4)) {
            this.notifyWireNeighborsOfNeighborChange(var1, var2 - 1, var3 + 1, var4);
        } else {
            this.notifyWireNeighborsOfNeighborChange(var1, var2 - 1, var3 - 1, var4);
        }

        if (var1.isBlockNormalCube(var2 + 1, var3, var4)) {
            this.notifyWireNeighborsOfNeighborChange(var1, var2 + 1, var3 + 1, var4);
        } else {
            this.notifyWireNeighborsOfNeighborChange(var1, var2 + 1, var3 - 1, var4);
        }

        if (var1.isBlockNormalCube(var2, var3, var4 - 1)) {
            this.notifyWireNeighborsOfNeighborChange(var1, var2, var3 + 1, var4 - 1);
        } else {
            this.notifyWireNeighborsOfNeighborChange(var1, var2, var3 - 1, var4 - 1);
        }

        if (var1.isBlockNormalCube(var2, var3, var4 + 1)) {
            this.notifyWireNeighborsOfNeighborChange(var1, var2, var3 + 1, var4 + 1);
        } else {
            this.notifyWireNeighborsOfNeighborChange(var1, var2, var3 - 1, var4 + 1);
        }

    }

    private int getMaxCurrentStrength(World var1, int var2, int var3, int var4, int var5) {
        if (var1.getBlockId(var2, var3, var4) != this.blockID) {
            return var5;
        }

        int var6 = var1.getBlockMetadata(var2, var3, var4);
        return Math.max(var6, var5);
    }

    @Override
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
        if (var1.localWorld) {
            return;
        }

        int var6 = var1.getBlockMetadata(var2, var3, var4);
        boolean var7 = this.canPlaceBlockAt(var1, var2, var3, var4);
        if (!var7) {
            this.dropBlockAsItem(var1, var2, var3, var4, var6);
            var1.setBlockWithNotify(var2, var3, var4, 0);
        } else {
            this.updateAndPropagateCurrentStrength(var1, var2, var3, var4);
        }

        super.onNeighborBlockChange(var1, var2, var3, var4, var5);
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return Item.REDSTONE.shiftedIndex;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int var5) {
        return this.wiresProvidePower && this.isPoweringTo(world, x, y, z, var5);
    }

    @Override
    public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int var5) {
        if (!this.wiresProvidePower) {
            return false;
        } else if (blockAccess.getBlockMetadata(x, y, z) == 0) {
            return false;
        } else if (var5 == 1) {
            return true;
        } else {
            boolean var6 = isPowerProviderOrWire(blockAccess, x - 1, y, z, 1) || !blockAccess.isBlockNormalCube(x - 1, y, z) && isPowerProviderOrWire(blockAccess, x - 1, y - 1, z, -1);
            boolean var7 = isPowerProviderOrWire(blockAccess, x + 1, y, z, 3) || !blockAccess.isBlockNormalCube(x + 1, y, z) && isPowerProviderOrWire(blockAccess, x + 1, y - 1, z, -1);
            boolean var8 = isPowerProviderOrWire(blockAccess, x, y, z - 1, 2) || !blockAccess.isBlockNormalCube(x, y, z - 1) && isPowerProviderOrWire(blockAccess, x, y - 1, z - 1, -1);
            boolean var9 = isPowerProviderOrWire(blockAccess, x, y, z + 1, 0) || !blockAccess.isBlockNormalCube(x, y, z + 1) && isPowerProviderOrWire(blockAccess, x, y - 1, z + 1, -1);
            if (!blockAccess.isBlockNormalCube(x, y + 1, z)) {
                if (blockAccess.isBlockNormalCube(x - 1, y, z) && isPowerProviderOrWire(blockAccess, x - 1, y + 1, z, -1)) {
                    var6 = true;
                }

                if (blockAccess.isBlockNormalCube(x + 1, y, z) && isPowerProviderOrWire(blockAccess, x + 1, y + 1, z, -1)) {
                    var7 = true;
                }

                if (blockAccess.isBlockNormalCube(x, y, z - 1) && isPowerProviderOrWire(blockAccess, x, y + 1, z - 1, -1)) {
                    var8 = true;
                }

                if (blockAccess.isBlockNormalCube(x, y, z + 1) && isPowerProviderOrWire(blockAccess, x, y + 1, z + 1, -1)) {
                    var9 = true;
                }
            }

            if (!var8 && !var7 && !var6 && !var9 && var5 >= 2 && var5 <= 5) {
                return true;
            } else if (var5 == 2 && var8 && !var6 && !var7) {
                return true;
            } else if (var5 == 3 && var9 && !var6 && !var7) {
                return true;
            } else if (var5 == 4 && var6 && !var8 && !var9) {
                return true;
            } else {
                return var5 == 5 && var7 && !var8 && !var9;
            }
        }
    }

    @Override
    public boolean canProvidePower() {
        return this.wiresProvidePower;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
        int var6 = var1.getBlockMetadata(var2, var3, var4);
        if (var6 > 0) {
            double var7 = (double) var2 + 0.5D + ((double) var5.nextFloat() - 0.5D) * 0.2D;
            double var9 = (float) var3 + 0.0625F;
            double var11 = (double) var4 + 0.5D + ((double) var5.nextFloat() - 0.5D) * 0.2D;
            float var13 = (float) var6 / 15.0F;
            float var14 = var13 * 0.6F + 0.4F;
            if (var6 == 0) {
                var14 = 0.0F;
            }

            float var15 = var13 * var13 * 0.7F - 0.5F;
            float var16 = var13 * var13 * 0.6F - 0.7F;
            if (var15 < 0.0F) {
                var15 = 0.0F;
            }

            if (var16 < 0.0F) {
                var16 = 0.0F;
            }

            var1.spawnParticle("reddust", var7, var9, var11, var14, var15, var16);
        }

    }
}
