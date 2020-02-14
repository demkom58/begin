package net.potion.tileentity;

import net.potion.entity.player.EntityPlayer;
import net.potion.inventory.IInventory;
import net.potion.item.ItemStack;
import net.potion.nbt.TagCompound;
import net.potion.nbt.TagList;

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
    public ItemStack decrStackSize(int var1, int var2) {
        if (this.chestContents[var1] != null) {
            if (this.chestContents[var1].stackSize <= var2) {
                ItemStack var4 = this.chestContents[var1];
                this.chestContents[var1] = null;
                this.onInventoryChanged();
                return var4;
            } else {
                ItemStack var3 = this.chestContents[var1].splitStack(var2);
                if (this.chestContents[var1].stackSize == 0) {
                    this.chestContents[var1] = null;
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
        this.chestContents[var1] = var2;
        if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
            var2.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    @Override
    public String getInvName() {
        return "Chest";
    }

    @Override
    public void readFromNBT(TagCompound var1) {
        super.readFromNBT(var1);
        TagList var2 = var1.getTagList("Items");
        this.chestContents = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            TagCompound var4 = (TagCompound) var2.tagAt(var3);
            int var5 = var4.getByte("Slot") & 255;
            if (var5 >= 0 && var5 < this.chestContents.length) {
                this.chestContents[var5] = new ItemStack(var4);
            }
        }

    }

    @Override
    public void writeToNBT(TagCompound var1) {
        super.writeToNBT(var1);
        TagList var2 = new TagList();

        for (int var3 = 0; var3 < this.chestContents.length; ++var3) {
            if (this.chestContents[var3] != null) {
                TagCompound var4 = new TagCompound();
                var4.setByte("Slot", (byte) var3);
                this.chestContents[var3].writeToNBT(var4);
                var2.setTag(var4);
            }
        }

        var1.setTag("Items", var2);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1) {
        if (this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this) {
            return false;
        } else {
            return var1.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
        }
    }
}
