package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.TagCompound;
import net.minecraft.nbt.TagList;

import java.util.Random;

public class TileEntityDispenser extends TileEntity implements IInventory {
    private ItemStack[] dispenserContents = new ItemStack[9];
    private final Random dispenserRandom = new Random();

    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.dispenserContents[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        if (this.dispenserContents[slot] == null) {
            return null;
        }

        if (this.dispenserContents[slot].stackSize <= count) {
            ItemStack stack = this.dispenserContents[slot];
            this.dispenserContents[slot] = null;
            this.onInventoryChanged();
            return stack;
        }

        ItemStack splitStack = this.dispenserContents[slot].splitStack(count);
        if (this.dispenserContents[slot].stackSize == 0) {
            this.dispenserContents[slot] = null;
        }

        this.onInventoryChanged();
        return splitStack;
    }

    public ItemStack getRandomStackFromInventory() {
        int slot = -1;
        int var2 = 1;

        for (int i = 0; i < this.dispenserContents.length; ++i) {
            if (this.dispenserContents[i] != null && this.dispenserRandom.nextInt(var2++) == 0) {
                slot = i;
            }
        }

        if (slot >= 0) {
            return this.decrStackSize(slot, 1);
        }

        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.dispenserContents[slot] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    @Override
    public String getInvName() {
        return "Trap";
    }

    @Override
    public void readFromNBT(TagCompound tag) {
        super.readFromNBT(tag);
        TagList itemsTag = tag.getTagList("Items");
        this.dispenserContents = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < itemsTag.tagCount(); ++i) {
            TagCompound itemTag = (TagCompound) itemsTag.tagAt(i);
            int slot = itemTag.getByte("Slot") & 255;
            if (slot < this.dispenserContents.length) {
                this.dispenserContents[slot] = new ItemStack(itemTag);
            }
        }

    }

    @Override
    public void writeToNBT(TagCompound tag) {
        super.writeToNBT(tag);
        TagList itemsTag = new TagList();

        for (int i = 0; i < this.dispenserContents.length; ++i) {
            if (this.dispenserContents[i] != null) {
                TagCompound itemTag = new TagCompound();
                itemTag.setByte("Slot", (byte) i);
                this.dispenserContents[i].writeToNBT(itemTag);
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
