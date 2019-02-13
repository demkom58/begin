package net.minecraft;

public class RecipesTools {
    private String[][] recipePatterns = new String[][]{{"XXX", " # ", " # "}, {"X", "#", "#"}, {"XX", "X#", " #"}, {"XX", " #", " #"}};
    private Object[][] recipeItems = new Object[][]{{Block.planks, Block.cobblestone, Item.INGOT_IRON, Item.DIAMOND, Item.INGOT_GOLD}, {Item.PICKAXE_WOOD, Item.PICKAXE_STONE, Item.PICKAXE_IRON, Item.PICKAXE_DIAMOND, Item.PICKAXE_GOLD}, {Item.SHOVEL_WOOD, Item.SHOVEL_STONE, Item.SHOVEL_IRON, Item.SHOVEL_DIAMOND, Item.SHOVEL_GOLD}, {Item.AXE_WOOD, Item.AXE_STONE, Item.AXE_IRON, Item.AXE_DIAMOND, Item.AXE_GOLD}, {Item.HOE_WOOD, Item.HOE_STONE, Item.HOE_IRON, Item.HOE_DIAMOND, Item.HOE_GOLD}};

    public void addRecipes(CraftingManager var1) {
        for (int var2 = 0; var2 < this.recipeItems[0].length; ++var2) {
            Object var3 = this.recipeItems[0][var2];

            for (int var4 = 0; var4 < this.recipeItems.length - 1; ++var4) {
                Item var5 = (Item) this.recipeItems[var4 + 1][var2];
                var1.addRecipe(new ItemStack(var5), this.recipePatterns[var4], '#', Item.STICK, 'X', var3);
            }
        }

        var1.addRecipe(new ItemStack(Item.SHEARS), " #", "# ", '#', Item.INGOT_IRON);
    }
}
