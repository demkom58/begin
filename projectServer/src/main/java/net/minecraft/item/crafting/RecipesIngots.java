package net.minecraft.item.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesIngots {
    private Object[][] recipeItems = new Object[][]{{Block.BLOCK_GOLD, new ItemStack(Item.INGOT_GOLD, 9)}, {Block.BLOCK_IRON, new ItemStack(Item.INGOT_IRON, 9)}, {Block.BLOCK_DIAMOND, new ItemStack(Item.DIAMOND, 9)}, {Block.BLOCK_LAPIS, new ItemStack(Item.DYE_POWDER, 9, 4)}};

    public void addRecipes(CraftingManager var1) {
        for (int var2 = 0; var2 < this.recipeItems.length; ++var2) {
            Block var3 = (Block) this.recipeItems[var2][0];
            ItemStack var4 = (ItemStack) this.recipeItems[var2][1];
            var1.addRecipe(new ItemStack(var3), "###", "###", "###", '#', var4);
            var1.addRecipe(var4, "#", '#', var3);
        }

    }
}
