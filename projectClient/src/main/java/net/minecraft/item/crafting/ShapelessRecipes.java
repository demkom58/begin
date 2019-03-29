package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShapelessRecipes implements IRecipe {
    private final ItemStack craftingResult;
    private final List<ItemStack> recipe;

    public ShapelessRecipes(ItemStack result, List<ItemStack> recipe) {
        this.craftingResult = result;
        this.recipe = recipe;
    }

    public ItemStack getCraftingResult() {
        return this.craftingResult;
    }

    public boolean matches(InventoryCrafting inventoryCrafting) {
        List<ItemStack> var2 = new ArrayList<>(this.recipe);

        for (int var3 = 0; var3 < 3; ++var3) {
            for (int var4 = 0; var4 < 3; ++var4) {
                ItemStack var5 = inventoryCrafting.func_21103_b(var4, var3);
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

    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        return this.craftingResult.copy();
    }

    public int getRecipeSize() {
        return this.recipe.size();
    }
}
