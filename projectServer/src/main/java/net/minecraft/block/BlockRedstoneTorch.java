package net.minecraft.block;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockRedstoneTorch extends BlockTorch {
    private static List<RedstoneUpdateInfo> torchUpdates = new ArrayList<>();
    private boolean torchActive = false;

    protected BlockRedstoneTorch(int var1, int var2, boolean var3) {
        super(var1, var2);
        this.torchActive = var3;
        this.setTickOnLoad(true);
    }

    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        return var1 == 1 ? Block.REDSTONE_WIRE.getBlockTextureFromSideAndMetadata(var1, var2) : super.getBlockTextureFromSideAndMetadata(var1, var2);
    }

    private boolean checkForBurnout(World var1, int var2, int var3, int var4, boolean var5) {
        if (var5) {
            torchUpdates.add(new RedstoneUpdateInfo(var2, var3, var4, var1.getWorldTime()));
        }

        int var6 = 0;

        for (RedstoneUpdateInfo var8 : torchUpdates) {
            if (var8.x == var2 && var8.y == var3 && var8.z == var4) {
                ++var6;
                if (var6 >= 8) {
                    return true;
                }
            }
        }

        return false;
    }

    public int tickRate() {
        return 2;
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        if (world.getBlockMetadata(x, y, z) == 0) {
            super.onBlockAdded(world, x, y, z);
        }

        if (this.torchActive) {
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
        }

    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        if (this.torchActive) {
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
        }

    }

    public boolean isPoweringTo(IBlockAccess blockAccess, int var2, int var3, int var4, int var5) {
        if (!this.torchActive) {
            return false;
        } else {
            int var6 = blockAccess.getBlockMetadata(var2, var3, var4);
            if (var6 == 5 && var5 == 1) {
                return false;
            } else if (var6 == 3 && var5 == 3) {
                return false;
            } else if (var6 == 4 && var5 == 2) {
                return false;
            } else if (var6 == 1 && var5 == 5) {
                return false;
            } else {
                return var6 != 2 || var5 != 4;
            }
        }
    }

    private boolean func_30003_g(World var1, int var2, int var3, int var4) {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        if (var5 == 5 && var1.isBlockIndirectlyProvidingPowerTo(var2, var3 - 1, var4, 0)) {
            return true;
        } else if (var5 == 3 && var1.isBlockIndirectlyProvidingPowerTo(var2, var3, var4 - 1, 2)) {
            return true;
        } else if (var5 == 4 && var1.isBlockIndirectlyProvidingPowerTo(var2, var3, var4 + 1, 3)) {
            return true;
        } else if (var5 == 1 && var1.isBlockIndirectlyProvidingPowerTo(var2 - 1, var3, var4, 4)) {
            return true;
        } else {
            return var5 == 2 && var1.isBlockIndirectlyProvidingPowerTo(var2 + 1, var3, var4, 5);
        }
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        boolean var6 = this.func_30003_g(world, x, y, z);

        while (torchUpdates.size() > 0 && world.getWorldTime() - torchUpdates.get(0).updateTime > 100L) {
            torchUpdates.remove(0);
        }

        if (this.torchActive) {
            if (var6) {
                world.setBlockAndMetadataWithNotify(x, y, z, Block.TORCH_REDSTONE_IDLE.blockID, world.getBlockMetadata(x, y, z));
                if (this.checkForBurnout(world, x, y, z, true)) {
                    world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                    for (int var7 = 0; var7 < 5; ++var7) {
                        double var8 = (double) x + random.nextDouble() * 0.6D + 0.2D;
                        double var10 = (double) y + random.nextDouble() * 0.6D + 0.2D;
                        double var12 = (double) z + random.nextDouble() * 0.6D + 0.2D;
                        world.spawnParticle("smoke", var8, var10, var12, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        } else if (!var6 && !this.checkForBurnout(world, x, y, z, false)) {
            world.setBlockAndMetadataWithNotify(x, y, z, Block.TORCH_REDSTONE_ACTIVE.blockID, world.getBlockMetadata(x, y, z));
        }

    }

    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        super.onNeighborBlockChange(world, var2, var3, var4, var5);
        world.scheduleUpdateTick(var2, var3, var4, this.blockID, this.tickRate());
    }

    public boolean isIndirectlyPoweringTo(World world, int var2, int var3, int var4, int var5) {
        return var5 == 0 && this.isPoweringTo(world, var2, var3, var4, var5);
    }

    public int idDropped(int var1, Random random) {
        return Block.TORCH_REDSTONE_ACTIVE.blockID;
    }

    public boolean canProvidePower() {
        return true;
    }
}
