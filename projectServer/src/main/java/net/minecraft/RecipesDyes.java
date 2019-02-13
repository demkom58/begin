package net.minecraft;

public class RecipesDyes {
    public void addRecipes(CraftingManager var1) {
        for (int var2 = 0; var2 < 16; ++var2) {
            var1.addShapelessRecipe(new ItemStack(Block.CLOTH, 1, BlockCloth.func_21034_d(var2)), new ItemStack(Item.DYE_POWDER, 1, var2), new ItemStack(Item.ITEMS_LIST[Block.CLOTH.blockID], 1, 0));
        }

        var1.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 11), Block.PLANT_YELLOW);
        var1.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 1), Block.PLANT_RED);
        var1.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 3, 15), Item.BONE);
        var1.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 9), new ItemStack(Item.DYE_POWDER, 1, 1), new ItemStack(Item.DYE_POWDER, 1, 15));
        var1.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 14), new ItemStack(Item.DYE_POWDER, 1, 1), new ItemStack(Item.DYE_POWDER, 1, 11));
        var1.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 10), new ItemStack(Item.DYE_POWDER, 1, 2), new ItemStack(Item.DYE_POWDER, 1, 15));
        var1.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 8), new ItemStack(Item.DYE_POWDER, 1, 0), new ItemStack(Item.DYE_POWDER, 1, 15));
        var1.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 7), new ItemStack(Item.DYE_POWDER, 1, 8), new ItemStack(Item.DYE_POWDER, 1, 15));
        var1.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 3, 7), new ItemStack(Item.DYE_POWDER, 1, 0), new ItemStack(Item.DYE_POWDER, 1, 15), new ItemStack(Item.DYE_POWDER, 1, 15));
        var1.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 12), new ItemStack(Item.DYE_POWDER, 1, 4), new ItemStack(Item.DYE_POWDER, 1, 15));
        var1.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 6), new ItemStack(Item.DYE_POWDER, 1, 4), new ItemStack(Item.DYE_POWDER, 1, 2));
        var1.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 5), new ItemStack(Item.DYE_POWDER, 1, 4), new ItemStack(Item.DYE_POWDER, 1, 1));
        var1.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 13), new ItemStack(Item.DYE_POWDER, 1, 5), new ItemStack(Item.DYE_POWDER, 1, 9));
        var1.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 3, 13), new ItemStack(Item.DYE_POWDER, 1, 4), new ItemStack(Item.DYE_POWDER, 1, 1), new ItemStack(Item.DYE_POWDER, 1, 9));
        var1.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 4, 13), new ItemStack(Item.DYE_POWDER, 1, 4), new ItemStack(Item.DYE_POWDER, 1, 1), new ItemStack(Item.DYE_POWDER, 1, 1), new ItemStack(Item.DYE_POWDER, 1, 15));
    }
}
