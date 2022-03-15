package net.potion.item.crafting;

import net.potion.block.Block;
import net.potion.item.ItemStack;

public class RecipesCrafting {
    public void addRecipes(CraftingManager var1) {
        var1.addRecipe(new ItemStack(Block.CHEST), "###", "# #", "###", '#', Block.PLANKS);
        var1.addRecipe(new ItemStack(Block.FURNACE), "###", "# #", "###", '#', Block.COBBLESTONE);
        var1.addRecipe(new ItemStack(Block.WORKBENCH), "##", "##", '#', Block.PLANKS);
        var1.addRecipe(new ItemStack(Block.SAND_STONE), "##", "##", '#', Block.SAND);
    }
}
