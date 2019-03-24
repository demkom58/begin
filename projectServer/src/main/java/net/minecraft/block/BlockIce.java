package net.minecraft.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.material.Material;
import net.minecraft.world.World;

import java.util.Random;

public class BlockIce extends BlockBreakable {
    public BlockIce(int var1, int var2) {
        super(var1, var2, Material.ICE, false);
        this.slipperiness = 0.98F;
        this.setTickOnLoad(true);
    }

    public void harvestBlock(World world, EntityPlayer entityPlayer, int var3, int var4, int var5, int var6) {
        super.harvestBlock(world, entityPlayer, var3, var4, var5, var6);
        Material var7 = world.getBlockMaterial(var3, var4 - 1, var5);
        if (var7.getIsSolid() || var7.isLiquid()) {
            world.setBlockWithNotify(var3, var4, var5, Block.WATER_MOVING.blockID);
        }

    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (world.getSavedLightValue(EnumSkyBlock.BLOCK, x, y, z) > 11 - Block.LIGHT_OPACITY[this.blockID]) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
            world.setBlockWithNotify(x, y, z, Block.WATER_STILL.blockID);
        }

    }

    public int getMobilityFlag() {
        return 0;
    }
}
