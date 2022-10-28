package net.potion.item;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.entity.passive.EntityFish;
import net.potion.entity.player.EntityPlayer;
import net.potion.world.World;

public class ItemFishingRod extends Item {
    public ItemFishingRod(int var1) {
        super(var1);
        this.setMaxDamage(64);
        this.setMaxStackSize(1);
    }

    @Side(CodeSide.CLIENT)
    @Override
    public boolean isFull3D() {
        return true;
    }

    @Side(CodeSide.CLIENT)
    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
        if (var3.fishEntity != null) {
            int var4 = var3.fishEntity.catchFish();
            var1.damageItem(var4, var3);
            var3.swingItem();
        } else {
            var2.playSoundAtEntity(var3, "random.bow", 0.5F, 0.4F / (ITEM_RAND.nextFloat() * 0.4F + 0.8F));
            if (!var2.localWorld) {
                var2.entityJoinedWorld(new EntityFish(var2, var3));
            }

            var3.swingItem();
        }

        return var1;
    }
}
