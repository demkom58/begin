package net.minecraft;

public interface IRecipe {
    boolean matches(InventoryCrafting var1);

    ItemStack getCraftingResult(InventoryCrafting var1);

    int getRecipeSize();

    ItemStack getCraftingResult();
}
