package net.minecraft.src;

public class ContainerPlayer extends Container {
   public InventoryCrafting field_1620_a;
   public IInventory field_1619_b;
   public boolean field_20124_c;

   public ContainerPlayer(InventoryPlayer var1) {
      this(var1, true);
   }

   public ContainerPlayer(InventoryPlayer var1, boolean var2) {
      this.field_1620_a = new InventoryCrafting(this, 2, 2);
      this.field_1619_b = new InventoryCraftResult();
      this.field_20124_c = false;
      this.field_20124_c = var2;
      this.func_20117_a(new SlotCrafting(var1.field_844_g, this.field_1620_a, this.field_1619_b, 0, 144, 36));

      for(int var3 = 0; var3 < 2; ++var3) {
         for(int var4 = 0; var4 < 2; ++var4) {
            this.func_20117_a(new Slot(this.field_1620_a, var4 + var3 * 2, 88 + var4 * 18, 26 + var3 * 18));
         }
      }

      for(int var5 = 0; var5 < 4; ++var5) {
         this.func_20117_a(new SlotArmor(this, var1, var1.func_469_c() - 1 - var5, 8, 8 + var5 * 18, var5));
      }

      for(int var6 = 0; var6 < 3; ++var6) {
         for(int var8 = 0; var8 < 9; ++var8) {
            this.func_20117_a(new Slot(var1, var8 + (var6 + 1) * 9, 8 + var8 * 18, 84 + var6 * 18));
         }
      }

      for(int var7 = 0; var7 < 9; ++var7) {
         this.func_20117_a(new Slot(var1, var7, 8 + var7 * 18, 142));
      }

      this.func_1103_a(this.field_1620_a);
   }

   public void func_1103_a(IInventory var1) {
      this.field_1619_b.func_472_a(0, CraftingManager.func_1120_a().func_21188_a(this.field_1620_a));
   }

   public void func_1104_a(EntityPlayer var1) {
      super.func_1104_a(var1);

      for(int var2 = 0; var2 < 4; ++var2) {
         ItemStack var3 = this.field_1620_a.func_468_c(var2);
         if (var3 != null) {
            var1.func_449_a(var3);
            this.field_1620_a.func_472_a(var2, (ItemStack)null);
         }
      }

   }

   public boolean func_20120_b(EntityPlayer var1) {
      return true;
   }

   public ItemStack func_27279_a(int var1) {
      ItemStack var2 = null;
      Slot var3 = (Slot)this.field_20122_e.get(var1);
      if (var3 != null && var3.func_20005_c()) {
         ItemStack var4 = var3.func_777_b();
         var2 = var4.func_1102_e();
         if (var1 == 0) {
            this.func_28125_a(var4, 9, 45, true);
         } else if (var1 >= 9 && var1 < 36) {
            this.func_28125_a(var4, 36, 45, false);
         } else if (var1 >= 36 && var1 < 45) {
            this.func_28125_a(var4, 9, 36, false);
         } else {
            this.func_28125_a(var4, 9, 45, false);
         }

         if (var4.field_1615_a == 0) {
            var3.func_776_b((ItemStack)null);
         } else {
            var3.func_779_d();
         }

         if (var4.field_1615_a == var2.field_1615_a) {
            return null;
         }

         var3.func_4103_a(var4);
      }

      return var2;
   }
}
