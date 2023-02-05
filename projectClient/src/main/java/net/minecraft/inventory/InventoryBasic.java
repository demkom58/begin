package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class InventoryBasic implements IInventory {
    private final String inventoryTitle;
    private final int slotsCount;
    private final ItemStack[] inventoryContents;
    private List<IInvBasic> invBasics;

    public InventoryBasic(String var1, int var2) {
        this.inventoryTitle = var1;
        this.slotsCount = var2;
        this.inventoryContents = new ItemStack[var2];
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return this.inventoryContents[var1];
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2) {
        if (this.inventoryContents[var1] != null) {
            if (this.inventoryContents[var1].stackSize <= var2) {
                ItemStack var4 = this.inventoryContents[var1];
                this.inventoryContents[var1] = null;
                this.onInventoryChanged();
                return var4;
            } else {
                ItemStack var3 = this.inventoryContents[var1].splitStack(var2);
                if (this.inventoryContents[var1].stackSize == 0) {
                    this.inventoryContents[var1] = null;
                }

                this.onInventoryChanged();
                return var3;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        this.inventoryContents[var1] = var2;
        if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
            var2.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    @Override
    public int getSizeInventory() {
        return this.slotsCount;
    }

    @Override
    public String getInvName() {
        return this.inventoryTitle;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void onInventoryChanged() {
        if (this.invBasics != null) {
            for (int i = 0; i < this.invBasics.size(); ++i) {
                this.invBasics.get(i).onInventoryChanged(this);
            }
        }

    }

    @Override
    public boolean canInteractWith(EntityPlayer var1) {
        return true;
    }
}
