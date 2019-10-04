package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.world.World;

public class ItemSnowball extends Item {
    public ItemSnowball(int var1) {
        super(var1);
        this.maxStackSize = 16;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
        --var1.stackSize;
        var2.playSoundAtEntity(var3, "random.bow", 0.5F, 0.4F / (ITEM_RAND.nextFloat() * 0.4F + 0.8F));
        if (!var2.multiplayerWorld) {
            var2.entityJoinedWorld(new EntitySnowball(var2, var3));
        }

        return var1;
    }
}
