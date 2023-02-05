package net.minecraft.item.crafting;

import java.util.Comparator;

class RecipeSorter implements Comparator<IRecipe> {
    // $FF: synthetic field
    final CraftingManager craftingManager;

    RecipeSorter(CraftingManager var1) {
        this.craftingManager = var1;
    }

    public int compareRecipes(IRecipe var1, IRecipe var2) {
        if (var1 instanceof ShapelessRecipes && var2 instanceof ShapedRecipes) {
            return 1;
        }

        if (var2 instanceof ShapelessRecipes && var1 instanceof ShapedRecipes) {
            return -1;
        }

        if (var2.getRecipeSize() < var1.getRecipeSize()) {
            return -1;
        }

        return var2.getRecipeSize() > var1.getRecipeSize() ? 1 : 0;
    }

    @Override
    public int compare(IRecipe var1, IRecipe var2) {
        return this.compareRecipes(var1, var2);
    }
}
