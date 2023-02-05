package net.minecraft.entity;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface ICrafting {
    @Side(CodeSide.SERVER)
    void updateCraftingInventory(Container var1, List<ItemStack> var2);

    void updateCraftingInventorySlot(Container var1, int var2, ItemStack var3);

    void updateCraftingInventoryInfo(Container var1, int var2, int var3);
}
