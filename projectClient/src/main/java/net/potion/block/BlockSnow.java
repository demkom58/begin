package net.potion.block;

import net.potion.entity.item.EntityItem;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.Item;
import net.potion.item.ItemStack;
import net.potion.material.Material;
import net.potion.stats.StatList;
import net.potion.util.AxisAlignedBB;
import net.potion.world.IBlockAccess;
import net.potion.world.World;

import java.util.Random;

public class BlockSnow extends Block {
    protected BlockSnow(int var1, int var2) {
        super(var1, var2, Material.SNOW);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setTickOnLoad(true);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        int var5 = var1.getBlockMetadata(var2, var3, var4) & 7;
        return var5 >= 3 ? AxisAlignedBB.getBoundingBoxFromPool((double) var2 + this.minX, (double) var3 + this.minY, (double) var4 + this.minZ, (double) var2 + this.maxX, (float) var3 + 0.5F, (double) var4 + this.maxZ) : null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        int var5 = blockAccess.getBlockMetadata(x, y, z) & 7;
        float var6 = (float) (2 * (1 + var5)) / 16.0F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var6, 1.0F);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        int var5 = world.getBlockId(x, y - 1, z);
        return (var5 != 0 && Block.BLOCKS_LIST[var5].isOpaqueCube()) && world.getBlockMaterial(x, y - 1, z).getIsSolid();
    }

    @Override
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
        this.func_314_h(var1, var2, var3, var4);
    }

    private boolean func_314_h(World var1, int var2, int var3, int var4) {
        if (!this.canPlaceBlockAt(var1, var2, var3, var4)) {
            this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
            var1.setBlockWithNotify(var2, var3, var4, 0);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int blockId) {
        int var7 = Item.SNOWBALL.shiftedIndex;
        float var8 = 0.7F;
        double var9 = (double) (world.rand.nextFloat() * var8) + (double) (1.0F - var8) * 0.5D;
        double var11 = (double) (world.rand.nextFloat() * var8) + (double) (1.0F - var8) * 0.5D;
        double var13 = (double) (world.rand.nextFloat() * var8) + (double) (1.0F - var8) * 0.5D;
        EntityItem var15 = new EntityItem(world, (double) x + var9, (double) y + var11, (double) z + var13, new ItemStack(var7, 1, 0));
        var15.delayBeforeCanPickup = 10;
        world.entityJoinedWorld(var15);
        world.setBlockWithNotify(x, y, z, 0);
        player.addStat(StatList.mineBlockStatArray[this.blockID], 1);
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return Item.SNOWBALL.shiftedIndex;
    }

    @Override
    public int quantityDropped(Random var1) {
        return 0;
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        if (var1.getSavedLightValue(EnumSkyBlock.BLOCK, var2, var3, var4) > 11) {
            this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
            var1.setBlockWithNotify(var2, var3, var4, 0);
        }

    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return side == 1 || super.shouldSideBeRendered(blockAccess, x, y, z, side);
    }
}
