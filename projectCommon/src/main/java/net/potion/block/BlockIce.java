package net.potion.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.entity.player.EntityPlayer;
import net.potion.material.Material;
import net.potion.world.IBlockAccess;
import net.potion.world.World;

import java.util.Random;

public class BlockIce extends BlockBreakable {
    public BlockIce(int var1, int var2) {
        super(var1, var2, Material.ICE, false);
        this.slipperiness = 0.98F;
        this.setTickOnLoad(true);
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return super.shouldSideBeRendered(blockAccess, x, y, z, 1 - side);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int blockId) {
        super.harvestBlock(world, player, x, y, z, blockId);
        Material var7 = world.getBlockMaterial(x, y - 1, z);
        if (var7.getIsSolid() || var7.isLiquid()) {
            world.setBlockWithNotify(x, y, z, Block.WATER_MOVING.blockID);
        }

    }

    @Override
    public int quantityDropped(Random var1) {
        return 0;
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        if (var1.getSavedLightValue(EnumSkyBlock.BLOCK, var2, var3, var4) > 11 - Block.LIGHT_OPACITY[this.blockID]) {
            this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
            var1.setBlockWithNotify(var2, var3, var4, Block.WATER_STILL.blockID);
        }

    }

    @Override
    public int getMobilityFlag() {
        return 0;
    }
}
