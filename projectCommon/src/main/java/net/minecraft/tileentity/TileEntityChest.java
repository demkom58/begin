package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.TagCompound;
import net.minecraft.nbt.TagList;

public class TileEntityChest extends TileEntity implements IInventory {
    private ItemStack[] chestContents = new ItemStack[36];

    @Override
    public int getSizeInventory() {
        return 27;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return this.chestContents[var1];
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        if (this.chestContents[slot] == null) {
            return null;
        }

        if (this.chestContents[slot].stackSize <= count) {
            ItemStack stack = this.chestContents[slot];
            this.chestContents[slot] = null;
            this.onInventoryChanged();
            return stack;
        }

        ItemStack splitStack = this.chestContents[slot].splitStack(count);
        if (this.chestContents[slot].stackSize == 0) {
            this.chestContents[slot] = null;
        }

        this.onInventoryChanged();
        return splitStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.chestContents[slot] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    @Override
    public String getInvName() {
        return "Chest";
    }

    @Override
    public void readFromNBT(TagCompound tag) {
        super.readFromNBT(tag);
        TagList itemsTag = tag.getTagList("Items");
        this.chestContents = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < itemsTag.tagCount(); ++i) {
            TagCompound itemTag = (TagCompound) itemsTag.tagAt(i);
            int slot = itemTag.getByte("Slot") & 255;
            if (slot < this.chestContents.length) {
                this.chestContents[slot] = new ItemStack(itemTag);
            }
        }

    }

    @Override
    public void writeToNBT(TagCompound tag) {
        super.writeToNBT(tag);
        TagList itemsTag = new TagList();

        for (int i = 0; i < this.chestContents.length; ++i) {
            if (this.chestContents[i] != null) {
                TagCompound itemTag = new TagCompound();
                itemTag.setByte("Slot", (byte) i);
                this.chestContents[i].writeToNBT(itemTag);
                itemsTag.setTag(itemTag);
            }
        }

        tag.setTag("Items", itemsTag);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        if (this.world.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this) {
            return false;
        }

        return player.getDistanceSq(
                (double) this.xCoord + 0.5D,
                (double) this.yCoord + 0.5D,
                (double) this.zCoord + 0.5D
        ) <= 64.0D;
    }
}
