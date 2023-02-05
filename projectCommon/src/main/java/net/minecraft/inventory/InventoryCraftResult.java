package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryCraftResult implements IInventory {
    private ItemStack[] stackResult = new ItemStack[1];

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return this.stackResult[var1];
    }

    @Override
    public String getInvName() {
        return "Result";
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2) {
        if (this.stackResult[var1] == null) {
            return null;
        }

        ItemStack var3 = this.stackResult[var1];
        this.stackResult[var1] = null;
        return var3;
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        this.stackResult[var1] = var2;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void onInventoryChanged() {
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1) {
        return true;
    }
}
