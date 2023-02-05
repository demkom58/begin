package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryCrafting implements IInventory {
    private ItemStack[] stackList;
    private int field1;
    private Container eventHandler;

    public InventoryCrafting(Container var1, int var2, int var3) {
        int var4 = var2 * var3;
        this.stackList = new ItemStack[var4];
        this.eventHandler = var1;
        this.field1 = var2;
    }

    @Override
    public int getSizeInventory() {
        return this.stackList.length;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return var1 >= this.getSizeInventory() ? null : this.stackList[var1];
    }

    public ItemStack method1(int var1, int var2) {
        if (var1 >= 0 && var1 < this.field1) {
            int var3 = var1 + var2 * this.field1;
            return this.getStackInSlot(var3);
        } else {
            return null;
        }
    }

    @Override
    public String getInvName() {
        return "Crafting";
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2) {
        if (this.stackList[var1] == null) {
            return null;
        }

        if (this.stackList[var1].stackSize <= var2) {
            ItemStack var4 = this.stackList[var1];
            this.stackList[var1] = null;
            this.eventHandler.onCraftMatrixChanged(this);
            return var4;
        }

        ItemStack var3 = this.stackList[var1].splitStack(var2);
        if (this.stackList[var1].stackSize == 0) {
            this.stackList[var1] = null;
        }

        this.eventHandler.onCraftMatrixChanged(this);
        return var3;
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        this.stackList[var1] = var2;
        this.eventHandler.onCraftMatrixChanged(this);
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
