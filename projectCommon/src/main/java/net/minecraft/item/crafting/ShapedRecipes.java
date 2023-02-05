package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class ShapedRecipes implements IRecipe {
    public final int recipeOutputItemID;
    private int recipeWidth;
    private int recipeHeight;
    private ItemStack[] recipeItems;
    private ItemStack recipeOutput;

    public ShapedRecipes(int recipeWidth, int recipeHeight, ItemStack[] recipeItems, ItemStack recipeOutput) {
        this.recipeOutputItemID = recipeOutput.itemID;
        this.recipeWidth = recipeWidth;
        this.recipeHeight = recipeHeight;
        this.recipeItems = recipeItems;
        this.recipeOutput = recipeOutput;
    }

    @Override
    public ItemStack getRecipeResult() {
        return this.recipeOutput;
    }

    @Override
    public boolean matches(InventoryCrafting var1) {
        for (int y = 0; y <= 3 - this.recipeWidth; ++y) {
            for (int x = 0; x <= 3 - this.recipeHeight; ++x) {
                if (this.method1(var1, y, x, true)) {
                    return true;
                }

                if (this.method1(var1, y, x, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean method1(InventoryCrafting var1, int var2, int var3, boolean var4) {
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                int var7 = y - var2;
                int var8 = x - var3;
                ItemStack var9 = null;
                if (var7 >= 0 && var8 >= 0 && var7 < this.recipeWidth && var8 < this.recipeHeight) {
                    if (var4) {
                        var9 = this.recipeItems[this.recipeWidth - var7 - 1 + var8 * this.recipeWidth];
                    } else {
                        var9 = this.recipeItems[var7 + var8 * this.recipeWidth];
                    }
                }

                ItemStack var10 = var1.method1(y, x);
                if (var10 != null || var9 != null) {
                    if (var10 == null && var9 != null || var10 != null && var9 == null) {
                        return false;
                    }

                    if (var9.itemID != var10.itemID) {
                        return false;
                    }

                    if (var9.getItemDamage() != -1 && var9.getItemDamage() != var10.getItemDamage()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1) {
        return new ItemStack(this.recipeOutput.itemID, this.recipeOutput.stackSize, this.recipeOutput.getItemDamage());
    }

    @Override
    public int getRecipeSize() {
        return this.recipeWidth * this.recipeHeight;
    }
}
