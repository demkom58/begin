package net.minecraft;

public class RecipesTools {
    private String[][] recipePatterns = new String[][]{
            {"XXX", " # ", " # "},
            {"X", "#", "#"},
            {"XX", "X#", " #"},
            {"XX", " #", " #"}
    };
    private Object[][] recipeItems = new Object[][]{
            {Block.PLANKS, Block.COBBLESTONE, Item.INGOT_IRON, Item.DIAMOND, Item.INGOT_GOLD},
            {Item.PICKAXE_WOOD, Item.PICKAXE_STONE, Item.PICKAXE_IRON, Item.PICKAXE_DIAMOND, Item.PICKAXE_GOLD},
            {Item.SHOVEL_WOOD, Item.SHOVEL_STONE, Item.SHOVEL_IRON, Item.SHOVEL_DIAMOND, Item.SHOVEL_GOLD},
            {Item.AXE_WOOD, Item.AXE_STONE, Item.AXE_IRON, Item.AXE_DIAMOND, Item.AXE_GOLD},
            {Item.HOE_WOOD, Item.HOE_STONE, Item.HOE_IRON, Item.HOE_DIAMOND, Item.HOE_GOLD}
    };

    public void addRecipes(CraftingManager craftingManager) {
        for (int i = 0; i < this.recipeItems[0].length; ++i) {
            Object o = this.recipeItems[0][i];

            for (int j = 0; j < this.recipeItems.length - 1; ++j) {
                Item item = (Item) this.recipeItems[j + 1][i];
                craftingManager.addRecipe(new ItemStack(item), this.recipePatterns[j], '#', Item.STICK, 'X', o);
            }
        }

        craftingManager.addRecipe(new ItemStack(Item.SHEARS), " #", "# ", '#', Item.INGOT_IRON);
    }
}
