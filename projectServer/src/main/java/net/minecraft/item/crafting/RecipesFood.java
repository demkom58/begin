package net.minecraft.item.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesFood {
    public void addRecipes(CraftingManager var1) {
        var1.addRecipe(new ItemStack(Item.BOWL_SOUP), "Y", "X", "#", 'X', Block.MUSHROOM_BROWN, 'Y', Block.MUSHROOM_RED, '#', Item.BOWL_EMPTY);
        var1.addRecipe(new ItemStack(Item.BOWL_SOUP), "Y", "X", "#", 'X', Block.MUSHROOM_RED, 'Y', Block.MUSHROOM_BROWN, '#', Item.BOWL_EMPTY);
        var1.addRecipe(new ItemStack(Item.COOKIE, 8), "#X#", 'X', new ItemStack(Item.DYE_POWDER, 1, 3), '#', Item.WHEAT);
    }
}
