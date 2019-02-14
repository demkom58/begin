package net.minecraft;

public class RecipesCrafting {
    public void addRecipes(CraftingManager var1) {
        var1.addRecipe(new ItemStack(Block.CHEST), "###", "# #", "###", '#', Block.PLANKS);
        var1.addRecipe(new ItemStack(Block.FURNACE), "###", "# #", "###", '#', Block.COBBLESTONE);
        var1.addRecipe(new ItemStack(Block.WORKBENCH), "##", "##", '#', Block.PLANKS);
        var1.addRecipe(new ItemStack(Block.SAND_STONE), "##", "##", '#', Block.SAND);
    }
}
