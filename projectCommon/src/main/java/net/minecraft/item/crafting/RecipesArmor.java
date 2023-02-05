package net.minecraft.item.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesArmor {
    private final String[][] recipePatterns = new String[][]{{"XXX", "X X"}, {"X X", "XXX", "XXX"}, {"XXX", "X X", "X X"}, {"X X", "X X"}};
    private final Object[][] recipeItems = new Object[][]{{Item.LEATHER, Block.FIRE, Item.INGOT_IRON, Item.DIAMOND, Item.INGOT_GOLD}, {Item.HELMET_LEATHER, Item.HELMET_CHAIN, Item.HELMET_IRON, Item.HELMET_DIAMOND, Item.HELMET_GOLD}, {Item.CHESTPLATE_LEATHER, Item.CHESTPLATE_CHAIN, Item.CHESTPLATE_IRON, Item.CHESTPLATE_DIAMOND, Item.CHESTPLATE_GOLD}, {Item.LEGGINGS_LEATHER, Item.LEGGINGS_CHAIN, Item.LEGGINGS_IRON, Item.LEGGINGS_DIAMOND, Item.LEGGINGS_GOLD}, {Item.BOOTS_LEATHER, Item.BOOTS_CHAIN, Item.BOOTS_IRON, Item.BOOTS_DIAMOND, Item.BOOTS_GOLD}};

    public void addRecipes(CraftingManager var1) {
        for (int var2 = 0; var2 < this.recipeItems[0].length; ++var2) {
            Object var3 = this.recipeItems[0][var2];

            for (int var4 = 0; var4 < this.recipeItems.length - 1; ++var4) {
                Item var5 = (Item) this.recipeItems[var4 + 1][var2];
                var1.addRecipe(new ItemStack(var5), this.recipePatterns[var4], 'X', var3);
            }
        }

    }
}
