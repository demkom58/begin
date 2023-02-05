package net.minecraft.item.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesWeapons {
    private final String[][] recipePatterns = new String[][]{{"X", "X", "#"}};
    private final Object[][] recipeItems = new Object[][]{{Block.PLANKS, Block.COBBLESTONE, Item.INGOT_IRON, Item.DIAMOND, Item.INGOT_GOLD}, {Item.SWORD_WOOD, Item.SWORD_STONE, Item.SWORD_IRON, Item.SWORD_DIAMOND, Item.SWORD_GOLD}};

    public void addRecipes(CraftingManager manager) {
        for (int i = 0; i < this.recipeItems[0].length; ++i) {
            Object oItem = this.recipeItems[0][i];

            for (int j = 0; j < this.recipeItems.length - 1; ++j) {
                Item item = (Item) this.recipeItems[j + 1][i];
                manager.addRecipe(new ItemStack(item), this.recipePatterns[j], '#', Item.STICK, 'X', oItem);
            }
        }

        manager.addRecipe(new ItemStack(Item.BOW, 1), " #X", "# X", " #X", 'X', Item.SILK, '#', Item.STICK);
        manager.addRecipe(new ItemStack(Item.ARROW, 4), "X", "#", "Y", 'Y', Item.FEATHER, 'X', Item.FLINT, '#', Item.STICK);
    }
}
