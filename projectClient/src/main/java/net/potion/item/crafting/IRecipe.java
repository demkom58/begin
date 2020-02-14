package net.potion.item.crafting;

import net.potion.inventory.InventoryCrafting;
import net.potion.item.ItemStack;

public interface IRecipe {
    boolean matches(InventoryCrafting var1);

    ItemStack getCraftingResult(InventoryCrafting var1);

    int getRecipeSize();

    ItemStack getCraftingResult();
}
