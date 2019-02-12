package net.minecraft.src;

public class TileEntityChest extends TileEntity implements IInventory {
   private ItemStack[] chestContents = new ItemStack[36];

   public int getSizeInventory() {
      return 27;
   }

   public ItemStack getStackInSlot(int var1) {
      return this.chestContents[var1];
   }

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

   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.chestContents[var1] = var2;
      if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
         var2.stackSize = this.getInventoryStackLimit();
      }

      this.onInventoryChanged();
   }

   public String getInvName() {
      return "Chest";
   }

   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      NBTTagList var2 = compound.getTagList("Items");
      this.chestContents = new ItemStack[this.getSizeInventory()];

      for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
         NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
         int var5 = var4.getByte("Slot") & 255;
         if (var5 >= 0 && var5 < this.chestContents.length) {
            this.chestContents[var5] = new ItemStack(var4);
         }
      }

   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.chestContents.length; ++var3) {
         if (this.chestContents[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            this.chestContents[var3].writeToNBT(var4);
            var2.setTag(var4);
         }
      }

      compound.setTag("Items", var2);
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean canInteractWith(EntityPlayer var1) {
      if (this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this) {
         return false;
      } else {
         return var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
      }
   }
}
