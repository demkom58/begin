package net.potion.item.crafting;

import net.potion.block.Block;
import net.potion.item.Item;
import net.potion.item.ItemStack;

public class RecipesIngots {
    private final Object[][] recipeItems = new Object[][]{{Block.BLOCK_GOLD, new ItemStack(Item.INGOT_GOLD, 9)}, {Block.BLOCK_IRON, new ItemStack(Item.INGOT_IRON, 9)}, {Block.BLOCK_DIAMOND, new ItemStack(Item.DIAMOND, 9)}, {Block.BLOCK_LAPIS, new ItemStack(Item.DYE_POWDER, 9, 4)}};

    public void addRecipes(CraftingManager manager) {
        for (int i = 0; i < this.recipeItems.length; ++i) {
            Block block = (Block) this.recipeItems[i][0];
            ItemStack stack = (ItemStack) this.recipeItems[i][1];
            manager.addRecipe(new ItemStack(block), "###", "###", "###", '#', stack);
            manager.addRecipe(stack, "#", '#', block);
        }

    }
}
