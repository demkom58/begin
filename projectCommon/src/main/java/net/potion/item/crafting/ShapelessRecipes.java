package net.potion.item.crafting;

import net.potion.inventory.InventoryCrafting;
import net.potion.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShapelessRecipes implements IRecipe {
    private final ItemStack craftingResult;
    private final List<ItemStack> recipe;

    public ShapelessRecipes(ItemStack result, List<ItemStack> recipe) {
        this.craftingResult = result;
        this.recipe = recipe;
    }

    @Override
    public ItemStack getRecipeResult() {
        return this.craftingResult;
    }

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting) {
        List<ItemStack> var2 = new ArrayList<>(this.recipe);

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                ItemStack stack = inventoryCrafting.method1(x, y);
                if (stack != null) {
                    boolean var6 = false;

                    for (ItemStack var8 : var2) {
                        if (stack.itemID == var8.itemID && (var8.getItemDamage() == -1 || stack.getItemDamage() == var8.getItemDamage())) {
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

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        return this.craftingResult.copy();
    }

    @Override
    public int getRecipeSize() {
        return this.recipe.size();
    }
}
