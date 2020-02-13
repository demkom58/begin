package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class ItemHoe extends Item {
    public ItemHoe(int var1, EnumToolMaterial var2) {
        super(var1);
        this.maxStackSize = 1;
        this.setMaxDamage(var2.getMaxUses());
    }

    @Override
    public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7) {
        int var8 = var3.getBlockId(var4, var5, var6);
        int var9 = var3.getBlockId(var4, var5 + 1, var6);
        if ((var7 == 0 || var9 != 0 || var8 != Block.GRASS.blockID) && var8 != Block.DIRT.blockID) {
            return false;
        } else {
            Block var10 = Block.FARMLAND;
            var3.playSoundEffect((float) var4 + 0.5F, (float) var5 + 0.5F, (float) var6 + 0.5F, var10.stepSound.getFormattedName(), (var10.stepSound.getVolume() + 1.0F) / 2.0F, var10.stepSound.getPitch() * 0.8F);
            if (var3.singleplayerWorld) {
                return true;
            } else {
                var3.setBlockWithNotify(var4, var5, var6, var10.blockID);
                var1.damageItem(1, var2);
                return true;
            }
        }
    }
}
