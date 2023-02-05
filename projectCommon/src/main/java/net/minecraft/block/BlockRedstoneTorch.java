package net.minecraft.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockRedstoneTorch extends BlockTorch {
    private static final List<RedstoneUpdateInfo> torchUpdates = new ArrayList<>();
    private boolean torchActive = false;

    protected BlockRedstoneTorch(int var1, int var2, boolean var3) {
        super(var1, var2);
        this.torchActive = var3;
        this.setTickOnLoad(true);
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        return side == 1 ? Block.REDSTONE_WIRE.getBlockTextureFromSideAndMetadata(side, metadata) : super.getBlockTextureFromSideAndMetadata(side, metadata);
    }

    private boolean checkForBurnout(World var1, int var2, int var3, int var4, boolean var5) {
        if (var5) {
            torchUpdates.add(new RedstoneUpdateInfo(var2, var3, var4, var1.getWorldTime()));
        }

        int var6 = 0;

        for (int var7 = 0; var7 < torchUpdates.size(); ++var7) {
            RedstoneUpdateInfo var8 = torchUpdates.get(var7);
            if (var8.x == var2 && var8.y == var3 && var8.z == var4) {
                ++var6;
                if (var6 >= 8) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int tickRate() {
        return 2;
    }

    @Override
    public void onBlockAdded(World var1, int var2, int var3, int var4) {
        if (var1.getBlockMetadata(var2, var3, var4) == 0) {
            super.onBlockAdded(var1, var2, var3, var4);
        }

        if (this.torchActive) {
            var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2, var3 + 1, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2 - 1, var3, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2 + 1, var3, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2, var3, var4 - 1, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2, var3, var4 + 1, this.blockID);
        }

    }

    @Override
    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        if (this.torchActive) {
            var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2, var3 + 1, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2 - 1, var3, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2 + 1, var3, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2, var3, var4 - 1, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2, var3, var4 + 1, this.blockID);
        }

    }

    @Override
    public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int var5) {
        if (!this.torchActive) {
            return false;
        }

        int var6 = blockAccess.getBlockMetadata(x, y, z);
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

    private boolean isPowered(World var1, int var2, int var3, int var4) {
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

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        boolean var6 = this.isPowered(var1, var2, var3, var4);

        while (torchUpdates.size() > 0 && var1.getWorldTime() - torchUpdates.get(0).updateTime > 100L) {
            torchUpdates.remove(0);
        }

        if (this.torchActive) {
            if (var6) {
                var1.setBlockAndMetadataWithNotify(var2, var3, var4, Block.TORCH_REDSTONE_IDLE.blockID, var1.getBlockMetadata(var2, var3, var4));
                if (this.checkForBurnout(var1, var2, var3, var4, true)) {
                    var1.playSoundEffect((float) var2 + 0.5F, (float) var3 + 0.5F, (float) var4 + 0.5F, "random.fizz", 0.5F, 2.6F + (var1.rand.nextFloat() - var1.rand.nextFloat()) * 0.8F);

                    for (int var7 = 0; var7 < 5; ++var7) {
                        double var8 = (double) var2 + var5.nextDouble() * 0.6D + 0.2D;
                        double var10 = (double) var3 + var5.nextDouble() * 0.6D + 0.2D;
                        double var12 = (double) var4 + var5.nextDouble() * 0.6D + 0.2D;
                        var1.spawnParticle("smoke", var8, var10, var12, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        } else if (!var6 && !this.checkForBurnout(var1, var2, var3, var4, false)) {
            var1.setBlockAndMetadataWithNotify(var2, var3, var4, Block.TORCH_REDSTONE_ACTIVE.blockID, var1.getBlockMetadata(var2, var3, var4));
        }

    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int var5) {
        super.onNeighborBlockChange(world, x, y, z, var5);
        world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int var5) {
        return var5 == 0 && this.isPoweringTo(world, x, y, z, var5);
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return Block.TORCH_REDSTONE_ACTIVE.blockID;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
        if (!this.torchActive) {
            return;
        }

        int var6 = var1.getBlockMetadata(var2, var3, var4);
        double var7 = (double) ((float) var2 + 0.5F) + (double) (var5.nextFloat() - 0.5F) * 0.2D;
        double var9 = (double) ((float) var3 + 0.7F) + (double) (var5.nextFloat() - 0.5F) * 0.2D;
        double var11 = (double) ((float) var4 + 0.5F) + (double) (var5.nextFloat() - 0.5F) * 0.2D;
        double var13 = 0.2199999988079071D;
        double var15 = 0.27000001072883606D;
        if (var6 == 1) {
            var1.spawnParticle("reddust", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
        } else if (var6 == 2) {
            var1.spawnParticle("reddust", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
        } else if (var6 == 3) {
            var1.spawnParticle("reddust", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
        } else if (var6 == 4) {
            var1.spawnParticle("reddust", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
        } else {
            var1.spawnParticle("reddust", var7, var9, var11, 0.0D, 0.0D, 0.0D);
        }

    }
}
