package net.minecraft;

public class RecipesFood {
    public void addRecipes(CraftingManager var1) {
        var1.addRecipe(new ItemStack(Item.BOWL_SOUP), "Y", "X", "#", 'X', Block.mushroomBrown, 'Y', Block.mushroomRed, '#', Item.BOWL_EMPTY);
        var1.addRecipe(new ItemStack(Item.BOWL_SOUP), "Y", "X", "#", 'X', Block.mushroomRed, 'Y', Block.mushroomBrown, '#', Item.BOWL_EMPTY);
        var1.addRecipe(new ItemStack(Item.COOKIE, 8), "#X#", 'X', new ItemStack(Item.DYE_POWDER, 1, 3), '#', Item.WHEAT);
    }
}
