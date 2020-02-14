package net.potion.item.crafting;

import net.potion.block.Block;
import net.potion.item.Item;
import net.potion.item.ItemStack;

public class RecipesWeapons {
    private String[][] recipePatterns = new String[][]{{"X", "X", "#"}};
    private Object[][] recipeItems = new Object[][]{{Block.PLANKS, Block.COBBLESTONE, Item.INGOT_IRON, Item.DIAMOND, Item.INGOT_GOLD}, {Item.SWORD_WOOD, Item.SWORD_STONE, Item.SWORD_IRON, Item.SWORD_DIAMOND, Item.SWORD_GOLD}};

    public void addRecipes(CraftingManager var1) {
        for (int var2 = 0; var2 < this.recipeItems[0].length; ++var2) {
            Object var3 = this.recipeItems[0][var2];

            for (int var4 = 0; var4 < this.recipeItems.length - 1; ++var4) {
                Item var5 = (Item) this.recipeItems[var4 + 1][var2];
                var1.addRecipe(new ItemStack(var5), this.recipePatterns[var4], '#', Item.STICK, 'X', var3);
            }
        }

        var1.addRecipe(new ItemStack(Item.BOW, 1), " #X", "# X", " #X", 'X', Item.SILK, '#', Item.STICK);
        var1.addRecipe(new ItemStack(Item.ARROW, 4), "X", "#", "Y", 'Y', Item.FEATHER, 'X', Item.FLINT, '#', Item.STICK);
    }
}
