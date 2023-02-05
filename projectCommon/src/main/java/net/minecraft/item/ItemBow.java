package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;

public class ItemBow extends Item {
    public ItemBow(int var1) {
        super(var1);
        this.maxStackSize = 1;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
        if (var3.inventory.consumeInventoryItem(Item.ARROW.shiftedIndex)) {
            var2.playSoundAtEntity(var3, "random.bow", 1.0F, 1.0F / (ITEM_RAND.nextFloat() * 0.4F + 0.8F));
            if (!var2.localWorld) {
                var2.entityJoinedWorld(new EntityArrow(var2, var3));
            }
        }

        return var1;
    }
}
