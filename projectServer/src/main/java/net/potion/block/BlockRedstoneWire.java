package net.potion.block;

import net.potion.item.Item;
import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.world.IBlockAccess;
import net.potion.world.World;
import net.potion.world.chunk.ChunkPosition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BlockRedstoneWire extends Block {
    private boolean wiresProvidePower = true;
    private Set<ChunkPosition> field_21032_b = new HashSet<>();

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
            return var4 == ModelBed.field_22153_b[var6 & 3];
        }
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        return this.blockIndexInTexture;
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
    public boolean isACube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        return world.isBlockNormalCube(var2, var3 - 1, var4);
    }

    private void updateAndPropagateCurrentStrength(World world, int var2, int var3, int var4) {
        this.func_21031_a(world, var2, var3, var4, var2, var3, var4);
        ArrayList<ChunkPosition> positions = new ArrayList<>(this.field_21032_b);
        this.field_21032_b.clear();

        for (int i = 0; i < positions.size(); ++i) {
            ChunkPosition chunkPosition = positions.get(i);
            world.notifyBlocksOfNeighborChange(chunkPosition.x, chunkPosition.y, chunkPosition.z, this.blockID);
        }

    }

    private void func_21031_a(World var1, int var2, int var3, int var4, int var5, int var6, int var7) {
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
                    this.func_21031_a(var1, var19, var3, var20, var2, var3, var4);
                }

                var15 = this.getMaxCurrentStrength(var1, var19, var14, var20, -1);
                var9 = var1.getBlockMetadata(var2, var3, var4);
                if (var9 > 0) {
                    --var9;
                }

                if (var15 >= 0 && var15 != var9) {
                    this.func_21031_a(var1, var19, var14, var20, var2, var3, var4);
                }
            }

            if (var8 == 0 || var9 == 0) {
                this.field_21032_b.add(new ChunkPosition(var2, var3, var4));
                this.field_21032_b.add(new ChunkPosition(var2 - 1, var3, var4));
                this.field_21032_b.add(new ChunkPosition(var2 + 1, var3, var4));
                this.field_21032_b.add(new ChunkPosition(var2, var3 - 1, var4));
                this.field_21032_b.add(new ChunkPosition(var2, var3 + 1, var4));
                this.field_21032_b.add(new ChunkPosition(var2, var3, var4 - 1));
                this.field_21032_b.add(new ChunkPosition(var2, var3, var4 + 1));
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
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        if (!world.singleplayerWorld) {
            this.updateAndPropagateCurrentStrength(world, x, y, z);
            world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            this.notifyWireNeighborsOfNeighborChange(world, x - 1, y, z);
            this.notifyWireNeighborsOfNeighborChange(world, x + 1, y, z);
            this.notifyWireNeighborsOfNeighborChange(world, x, y, z - 1);
            this.notifyWireNeighborsOfNeighborChange(world, x, y, z + 1);
            if (world.isBlockNormalCube(x - 1, y, z)) {
                this.notifyWireNeighborsOfNeighborChange(world, x - 1, y + 1, z);
            } else {
                this.notifyWireNeighborsOfNeighborChange(world, x - 1, y - 1, z);
            }

            if (world.isBlockNormalCube(x + 1, y, z)) {
                this.notifyWireNeighborsOfNeighborChange(world, x + 1, y + 1, z);
            } else {
                this.notifyWireNeighborsOfNeighborChange(world, x + 1, y - 1, z);
            }

            if (world.isBlockNormalCube(x, y, z - 1)) {
                this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z - 1);
            } else {
                this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z - 1);
            }

            if (world.isBlockNormalCube(x, y, z + 1)) {
                this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z + 1);
            } else {
                this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z + 1);
            }

        }
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        super.onBlockRemoval(world, x, y, z);
        if (!world.singleplayerWorld) {
            world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            this.updateAndPropagateCurrentStrength(world, x, y, z);
            this.notifyWireNeighborsOfNeighborChange(world, x - 1, y, z);
            this.notifyWireNeighborsOfNeighborChange(world, x + 1, y, z);
            this.notifyWireNeighborsOfNeighborChange(world, x, y, z - 1);
            this.notifyWireNeighborsOfNeighborChange(world, x, y, z + 1);
            if (world.isBlockNormalCube(x - 1, y, z)) {
                this.notifyWireNeighborsOfNeighborChange(world, x - 1, y + 1, z);
            } else {
                this.notifyWireNeighborsOfNeighborChange(world, x - 1, y - 1, z);
            }

            if (world.isBlockNormalCube(x + 1, y, z)) {
                this.notifyWireNeighborsOfNeighborChange(world, x + 1, y + 1, z);
            } else {
                this.notifyWireNeighborsOfNeighborChange(world, x + 1, y - 1, z);
            }

            if (world.isBlockNormalCube(x, y, z - 1)) {
                this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z - 1);
            } else {
                this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z - 1);
            }

            if (world.isBlockNormalCube(x, y, z + 1)) {
                this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z + 1);
            } else {
                this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z + 1);
            }

        }
    }

    private int getMaxCurrentStrength(World var1, int var2, int var3, int var4, int var5) {
        if (var1.getBlockId(var2, var3, var4) != this.blockID) {
            return var5;
        } else {
            int var6 = var1.getBlockMetadata(var2, var3, var4);
            return Math.max(var6, var5);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        if (!world.singleplayerWorld) {
            int var6 = world.getBlockMetadata(var2, var3, var4);
            boolean var7 = this.canPlaceBlockAt(world, var2, var3, var4);
            if (!var7) {
                this.dropBlockAsItem(world, var2, var3, var4, var6);
                world.setBlockWithNotify(var2, var3, var4, 0);
            } else {
                this.updateAndPropagateCurrentStrength(world, var2, var3, var4);
            }

            super.onNeighborBlockChange(world, var2, var3, var4, var5);
        }
    }

    @Override
    public int idDropped(int var1, Random random) {
        return Item.REDSTONE.shiftedIndex;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int var2, int var3, int var4, int var5) {
        return this.wiresProvidePower && this.isPoweringTo(world, var2, var3, var4, var5);
    }

    @Override
    public boolean isPoweringTo(IBlockAccess blockAccess, int var2, int var3, int var4, int var5) {
        if (!this.wiresProvidePower) {
            return false;
        } else if (blockAccess.getBlockMetadata(var2, var3, var4) == 0) {
            return false;
        } else if (var5 == 1) {
            return true;
        } else {
            boolean var6 = isPowerProviderOrWire(blockAccess, var2 - 1, var3, var4, 1) || !blockAccess.isBlockNormalCube(var2 - 1, var3, var4) && isPowerProviderOrWire(blockAccess, var2 - 1, var3 - 1, var4, -1);
            boolean var7 = isPowerProviderOrWire(blockAccess, var2 + 1, var3, var4, 3) || !blockAccess.isBlockNormalCube(var2 + 1, var3, var4) && isPowerProviderOrWire(blockAccess, var2 + 1, var3 - 1, var4, -1);
            boolean var8 = isPowerProviderOrWire(blockAccess, var2, var3, var4 - 1, 2) || !blockAccess.isBlockNormalCube(var2, var3, var4 - 1) && isPowerProviderOrWire(blockAccess, var2, var3 - 1, var4 - 1, -1);
            boolean var9 = isPowerProviderOrWire(blockAccess, var2, var3, var4 + 1, 0) || !blockAccess.isBlockNormalCube(var2, var3, var4 + 1) && isPowerProviderOrWire(blockAccess, var2, var3 - 1, var4 + 1, -1);
            if (!blockAccess.isBlockNormalCube(var2, var3 + 1, var4)) {
                if (blockAccess.isBlockNormalCube(var2 - 1, var3, var4) && isPowerProviderOrWire(blockAccess, var2 - 1, var3 + 1, var4, -1)) {
                    var6 = true;
                }

                if (blockAccess.isBlockNormalCube(var2 + 1, var3, var4) && isPowerProviderOrWire(blockAccess, var2 + 1, var3 + 1, var4, -1)) {
                    var7 = true;
                }

                if (blockAccess.isBlockNormalCube(var2, var3, var4 - 1) && isPowerProviderOrWire(blockAccess, var2, var3 + 1, var4 - 1, -1)) {
                    var8 = true;
                }

                if (blockAccess.isBlockNormalCube(var2, var3, var4 + 1) && isPowerProviderOrWire(blockAccess, var2, var3 + 1, var4 + 1, -1)) {
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
}
