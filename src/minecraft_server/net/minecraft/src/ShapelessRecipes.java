package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class ShapelessRecipes implements IRecipe {
    private final ItemStack result;
    private final List<ItemStack> recipe;

    public ShapelessRecipes(ItemStack result, List<ItemStack> recipe) {
        this.result = result;
        this.recipe = recipe;
    }

    public ItemStack func_25077_b() {
        return this.result;
    }

    public boolean func_21134_a(InventoryCrafting var1) {
        List<ItemStack> var2 = new ArrayList<>(this.recipe);

        for (int var3 = 0; var3 < 3; ++var3) {
            for (int var4 = 0; var4 < 3; ++var4) {
                ItemStack var5 = var1.func_21084_a(var4, var3);
                if (var5 != null) {
                    boolean var6 = false;

                    for (ItemStack var8 : var2) {
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

    public ItemStack func_21136_b(InventoryCrafting var1) {
        return this.result.copy();
    }

    public int getRecipeSize() {
        return this.recipe.size();
    }
}
