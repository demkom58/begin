package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class ShapelessRecipes implements IRecipe {
   private final ItemStack recipeOutput;
   private final List<ItemStack> recipeItems;

   public ShapelessRecipes(ItemStack var1, List<ItemStack> var2) {
      this.recipeOutput = var1;
      this.recipeItems = var2;
   }

   public ItemStack getRecipeOutput() {
      return this.recipeOutput;
   }

   public boolean matches(InventoryCrafting var1) {
      List<ItemStack> var2 = new ArrayList<>(this.recipeItems);

      for(int var3 = 0; var3 < 3; ++var3) {
         for(int var4 = 0; var4 < 3; ++var4) {
            ItemStack var5 = var1.func_21103_b(var4, var3);
            if (var5 != null) {
               boolean var6 = false;

               for(ItemStack var8 : var2) {
                  if (var5.itemID == var8.itemID && (var8.getItemDamage() == -1 || var5.getItemDamage() == var8.getItemDamage())) {
                     var6 = true;
                     var2.remove(var8);
                     break;
                  }
               }

               if (!var6) {
                  return false;
               }
            }
         }
      }

      return var2.isEmpty();
   }

   public ItemStack getCraftingResult(InventoryCrafting var1) {
      return this.recipeOutput.copy();
   }

   public int getRecipeSize() {
      return this.recipeItems.size();
   }
}
