package net.potion.item;

import net.potion.block.Block;
import net.potion.entity.player.EntityPlayer;
import net.potion.world.World;

public class ItemSeeds extends Item {
    private int blockId;

    public ItemSeeds(int var1, int blockId) {
        super(var1);
        this.blockId = blockId;
    }

    @Override
    public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7) {
        if (var7 != 1) {
            return false;
        }

        int var8 = var3.getBlockId(var4, var5, var6);
        if (var8 == Block.FARMLAND.blockID && var3.isAirBlock(var4, var5 + 1, var6)) {
            var3.setBlockWithNotify(var4, var5 + 1, var6, this.blockId);
            --var1.stackSize;
            return true;
        }

        return false;
    }
}
