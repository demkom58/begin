package net.potion.item;

import net.potion.entity.EntityLiving;
import net.potion.entity.passive.EntityPig;

public class ItemSaddle extends Item {
    public ItemSaddle(int var1) {
        super(var1);
        this.maxStackSize = 1;
    }

    @Override
    public void saddleEntity(ItemStack var1, EntityLiving var2) {
        if (var2 instanceof EntityPig) {
            EntityPig var3 = (EntityPig) var2;
            if (!var3.getSaddled()) {
                var3.setSaddled(true);
                --var1.stackSize;
            }
        }

    }

    @Override
    public boolean hitEntity(ItemStack var1, EntityLiving var2, EntityLiving var3) {
        this.saddleEntity(var1, var2);
        return true;
    }
}
