package net.minecraft.inventory;

import net.minecraft.block.Block;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

class SlotArmor extends Slot {
    // $FF: synthetic field
    final int armorType;
    // $FF: synthetic field
    final ContainerPlayer inventory;

    SlotArmor(ContainerPlayer inventory, IInventory var2, int var3, int var4, int var5, int armorType) {
        super(var2, var3, var4, var5);
        this.inventory = inventory;
        this.armorType = armorType;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack var1) {
        if (var1.getItem() instanceof ItemArmor) {
            return ((ItemArmor) var1.getItem()).armorType == this.armorType;
        } else if (var1.getItem().shiftedIndex == Block.PUMPKIN.blockID) {
            return this.armorType == 0;
        } else {
            return false;
        }
    }
}
