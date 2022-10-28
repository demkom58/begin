package net.potion.inventory;

import net.potion.achievement.AchievementList;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.Item;
import net.potion.item.ItemStack;

public class SlotFurnace extends Slot {
    private final EntityPlayer thePlayer;

    public SlotFurnace(EntityPlayer var1, IInventory var2, int var3, int var4, int var5) {
        super(var2, var3, var4, var5);
        this.thePlayer = var1;
    }

    @Override
    public boolean isItemValid(ItemStack var1) {
        return false;
    }

    @Override
    public void onPickupFromSlot(ItemStack var1) {
        var1.onCrafting(this.thePlayer.world, this.thePlayer);
        if (var1.itemID == Item.INGOT_IRON.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.acquireIron, 1);
        }

        if (var1.itemID == Item.FISH_COOKED.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.cookFish, 1);
        }

        super.onPickupFromSlot(var1);
    }
}
