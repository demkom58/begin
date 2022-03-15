package net.potion.item;

import net.potion.entity.player.EntityPlayer;
import net.potion.world.World;

public class ItemSoup extends ItemFood {
    public ItemSoup(int var1, int var2) {
        super(var1, var2, false);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
        super.onItemRightClick(var1, var2, var3);
        return new ItemStack(Item.BOWL_EMPTY);
    }
}
