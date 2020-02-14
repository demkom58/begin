package net.potion.item;

import net.potion.entity.EntityLiving;
import net.potion.block.Block;

public class ItemShears extends Item {
    public ItemShears(int var1) {
        super(var1);
        this.setMaxStackSize(1);
        this.setMaxDamage(238);
    }

    @Override
    public boolean func_25007_a(ItemStack var1, int var2, int var3, int var4, int var5, EntityLiving var6) {
        if (var2 == Block.LEAVES.blockID || var2 == Block.WEB.blockID) {
            var1.damageItem(1, var6);
        }

        return super.func_25007_a(var1, var2, var3, var4, var5, var6);
    }

    @Override
    public boolean canHarvestBlock(Block var1) {
        return var1.blockID == Block.WEB.blockID;
    }

    @Override
    public float getStrVsBlock(ItemStack var1, Block var2) {
        if (var2.blockID != Block.WEB.blockID && var2.blockID != Block.LEAVES.blockID) {
            return var2.blockID == Block.CLOTH.blockID ? 5.0F : super.getStrVsBlock(var1, var2);
        } else {
            return 15.0F;
        }
    }
}
