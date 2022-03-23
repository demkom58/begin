package net.potion.inventory;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.item.ItemStack;

public class Slot {
    private final int slotIndex;
    private final IInventory inventory;
    public int slotNumber;
    public int xDisplayPosition;
    public int yDisplayPosition;

    public Slot(IInventory inventory, int slotIndex, int x, int y) {
        this.inventory = inventory;
        this.slotIndex = slotIndex;
        this.xDisplayPosition = x;
        this.yDisplayPosition = y;
    }

    public void onPickupFromSlot(ItemStack var1) {
        this.onSlotChanged();
    }

    public boolean isItemValid(ItemStack var1) {
        return true;
    }

    public ItemStack getStack() {
        return this.inventory.getStackInSlot(this.slotIndex);
    }

    public boolean hasStack() {
        return this.getStack() != null;
    }

    public void putStack(ItemStack var1) {
        this.inventory.setInventorySlotContents(this.slotIndex, var1);
        this.onSlotChanged();
    }

    public void onSlotChanged() {
        this.inventory.onInventoryChanged();
    }

    public int getSlotStackLimit() {
        return this.inventory.getInventoryStackLimit();
    }

    @Side(CodeSide.CLIENT)
    public int getBackgroundIconIndex() {
        return -1;
    }

    public ItemStack decrStackSize(int var1) {
        return this.inventory.decrStackSize(this.slotIndex, var1);
    }

    public boolean isHere(IInventory inventory, int slotIndex) {
        return inventory == this.inventory && slotIndex == this.slotIndex;
    }

}
