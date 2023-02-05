package net.minecraft.item.crafting;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCloth;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesDyes {
    public void addRecipes(CraftingManager manager) {
        for (int i = 0; i < 16; ++i) {
            manager.addShapelessRecipe(new ItemStack(Block.CLOTH, 1, BlockCloth.method2(i)), new ItemStack(Item.DYE_POWDER, 1, i), new ItemStack(Item.ITEMS_LIST[Block.CLOTH.blockID], 1, 0));
        }

        manager.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 11), Block.PLANT_YELLOW);
        manager.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 1), Block.PLANT_RED);
        manager.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 3, 15), Item.BONE);
        manager.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 9), new ItemStack(Item.DYE_POWDER, 1, 1), new ItemStack(Item.DYE_POWDER, 1, 15));
        manager.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 14), new ItemStack(Item.DYE_POWDER, 1, 1), new ItemStack(Item.DYE_POWDER, 1, 11));
        manager.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 10), new ItemStack(Item.DYE_POWDER, 1, 2), new ItemStack(Item.DYE_POWDER, 1, 15));
        manager.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 8), new ItemStack(Item.DYE_POWDER, 1, 0), new ItemStack(Item.DYE_POWDER, 1, 15));
        manager.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 7), new ItemStack(Item.DYE_POWDER, 1, 8), new ItemStack(Item.DYE_POWDER, 1, 15));
        manager.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 3, 7), new ItemStack(Item.DYE_POWDER, 1, 0), new ItemStack(Item.DYE_POWDER, 1, 15), new ItemStack(Item.DYE_POWDER, 1, 15));
        manager.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 12), new ItemStack(Item.DYE_POWDER, 1, 4), new ItemStack(Item.DYE_POWDER, 1, 15));
        manager.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 6), new ItemStack(Item.DYE_POWDER, 1, 4), new ItemStack(Item.DYE_POWDER, 1, 2));
        manager.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 5), new ItemStack(Item.DYE_POWDER, 1, 4), new ItemStack(Item.DYE_POWDER, 1, 1));
        manager.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 2, 13), new ItemStack(Item.DYE_POWDER, 1, 5), new ItemStack(Item.DYE_POWDER, 1, 9));
        manager.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 3, 13), new ItemStack(Item.DYE_POWDER, 1, 4), new ItemStack(Item.DYE_POWDER, 1, 1), new ItemStack(Item.DYE_POWDER, 1, 9));
        manager.addShapelessRecipe(new ItemStack(Item.DYE_POWDER, 4, 13), new ItemStack(Item.DYE_POWDER, 1, 4), new ItemStack(Item.DYE_POWDER, 1, 1), new ItemStack(Item.DYE_POWDER, 1, 1), new ItemStack(Item.DYE_POWDER, 1, 15));
    }
}
