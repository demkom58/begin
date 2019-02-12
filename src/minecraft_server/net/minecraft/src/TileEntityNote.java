package net.minecraft.src;

public class TileEntityNote extends TileEntity {
   public byte note = 0;
   public boolean previousRedstoneState = false;

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      compound.setByte("note", this.note);
   }

   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.note = compound.getByte("note");
      if (this.note < 0) {
         this.note = 0;
      }

      if (this.note > 24) {
         this.note = 24;
      }

   }

   public void changePitch() {
      this.note = (byte)((this.note + 1) % 25);
      this.onInventoryChanged();
   }

   public void triggerNote(World var1, int var2, int var3, int var4) {
      if (var1.getBlockMaterial(var2, var3 + 1, var4) == Material.air) {
         Material var5 = var1.getBlockMaterial(var2, var3 - 1, var4);
         byte var6 = 0;
         if (var5 == Material.rock) {
            var6 = 1;
         }

         if (var5 == Material.sand) {
            var6 = 2;
         }

         if (var5 == Material.glass) {
            var6 = 3;
         }

         if (var5 == Material.wood) {
            var6 = 4;
         }

         var1.playNoteAt(var2, var3, var4, var6, this.note);
      }
   }
}
