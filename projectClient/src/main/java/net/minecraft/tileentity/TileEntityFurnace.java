package net.minecraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.material.Material;
import net.minecraft.nbt.TagCompound;
import net.minecraft.nbt.TagList;

public class TileEntityFurnace extends TileEntity implements IInventory {
    public int furnaceBurnTime = 0;
    public int currentItemBurnTime = 0;
    public int furnaceCookTime = 0;
    private ItemStack[] furnaceItemStacks = new ItemStack[3];

    @Override
    public int getSizeInventory() {
        return this.furnaceItemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return this.furnaceItemStacks[var1];
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2) {
        if (this.furnaceItemStacks[var1] != null) {
            if (this.furnaceItemStacks[var1].stackSize <= var2) {
                ItemStack var4 = this.furnaceItemStacks[var1];
                this.furnaceItemStacks[var1] = null;
                return var4;
            } else {
                ItemStack var3 = this.furnaceItemStacks[var1].splitStack(var2);
                if (this.furnaceItemStacks[var1].stackSize == 0) {
                    this.furnaceItemStacks[var1] = null;
                }

                return var3;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        this.furnaceItemStacks[var1] = var2;
        if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
            var2.stackSize = this.getInventoryStackLimit();
        }

    }

    @Override
    public String getInvName() {
        return "Furnace";
    }

    @Override
    public void readFromNBT(TagCompound var1) {
        super.readFromNBT(var1);
        TagList var2 = var1.getTagList("Items");
        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            TagCompound var4 = (TagCompound) var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");
            if (var5 >= 0 && var5 < this.furnaceItemStacks.length) {
                this.furnaceItemStacks[var5] = new ItemStack(var4);
            }
        }

        this.furnaceBurnTime = var1.getShort("BurnTime");
        this.furnaceCookTime = var1.getShort("CookTime");
        this.currentItemBurnTime = this.getItemBurnTime(this.furnaceItemStacks[1]);
    }

    @Override
    public void writeToNBT(TagCompound var1) {
        super.writeToNBT(var1);
        var1.setShort("BurnTime", (short) this.furnaceBurnTime);
        var1.setShort("CookTime", (short) this.furnaceCookTime);
        TagList var2 = new TagList();

        for (int var3 = 0; var3 < this.furnaceItemStacks.length; ++var3) {
            if (this.furnaceItemStacks[var3] != null) {
                TagCompound var4 = new TagCompound();
                var4.setByte("Slot", (byte) var3);
                this.furnaceItemStacks[var3].writeToNBT(var4);
                var2.setTag(var4);
            }
        }

        var1.setTag("Items", var2);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public int getCookProgressScaled(int var1) {
        return this.furnaceCookTime * var1 / 200;
    }

    public int getBurnTimeRemainingScaled(int var1) {
        if (this.currentItemBurnTime == 0) {
            this.currentItemBurnTime = 200;
        }

        return this.furnaceBurnTime * var1 / this.currentItemBurnTime;
    }

    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    @Override
    public void updateEntity() {
        boolean var1 = this.furnaceBurnTime > 0;
        boolean var2 = false;
        if (this.furnaceBurnTime > 0) {
            --this.furnaceBurnTime;
        }

        if (!this.worldObj.multiplayerWorld) {
            if (this.furnaceBurnTime == 0 && this.canSmelt()) {
                this.currentItemBurnTime = this.furnaceBurnTime = this.getItemBurnTime(this.furnaceItemStacks[1]);
                if (this.furnaceBurnTime > 0) {
                    var2 = true;
                    if (this.furnaceItemStacks[1] != null) {
                        --this.furnaceItemStacks[1].stackSize;
                        if (this.furnaceItemStacks[1].stackSize == 0) {
                            this.furnaceItemStacks[1] = null;
                        }
                    }
                }
            }

            if (this.isBurning() && this.canSmelt()) {
                ++this.furnaceCookTime;
                if (this.furnaceCookTime == 200) {
                    this.furnaceCookTime = 0;
                    this.smeltItem();
                    var2 = true;
                }
            } else {
                this.furnaceCookTime = 0;
            }

            if (var1 != this.furnaceBurnTime > 0) {
                var2 = true;
                BlockFurnace.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }

        if (var2) {
            this.onInventoryChanged();
        }

    }

    private boolean canSmelt() {
        if (this.furnaceItemStacks[0] == null) {
            return false;
        } else {
            ItemStack var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0].getItem().shiftedIndex);
            if (var1 == null) {
                return false;
            } else if (this.furnaceItemStacks[2] == null) {
                return true;
            } else if (!this.furnaceItemStacks[2].isItemEqual(var1)) {
                return false;
            } else if (this.furnaceItemStacks[2].stackSize < this.getInventoryStackLimit() && this.furnaceItemStacks[2].stackSize < this.furnaceItemStacks[2].getMaxStackSize()) {
                return true;
            } else {
                return this.furnaceItemStacks[2].stackSize < var1.getMaxStackSize();
            }
        }
    }

    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0].getItem().shiftedIndex);
            if (this.furnaceItemStacks[2] == null) {
                this.furnaceItemStacks[2] = var1.copy();
            } else if (this.furnaceItemStacks[2].itemID == var1.itemID) {
                ++this.furnaceItemStacks[2].stackSize;
            }

            --this.furnaceItemStacks[0].stackSize;
            if (this.furnaceItemStacks[0].stackSize <= 0) {
                this.furnaceItemStacks[0] = null;
            }

        }
    }

    private int getItemBurnTime(ItemStack var1) {
        if (var1 == null) {
            return 0;
        } else {
            int var2 = var1.getItem().shiftedIndex;
            if (var2 < 256 && Block.BLOCKS_LIST[var2].blockMaterial == Material.WOOD) {
                return 300;
            } else if (var2 == Item.STICK.shiftedIndex) {
                return 100;
            } else if (var2 == Item.COAL.shiftedIndex) {
                return 1600;
            } else if (var2 == Item.BUCKET_LAVA.shiftedIndex) {
                return 20000;
            } else {
                return var2 == Block.SAPLING.blockID ? 100 : 0;
            }
        }
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
