package net.minecraft.item.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class FurnaceRecipes {
    private static final FurnaceRecipes smeltingBase = new FurnaceRecipes();
    private Map<Integer, ItemStack> smeltingList = new HashMap<>();

    private FurnaceRecipes() {
        this.addSmelting(Block.ORE_IRON.blockID, new ItemStack(Item.INGOT_IRON));
        this.addSmelting(Block.ORE_GOLD.blockID, new ItemStack(Item.INGOT_GOLD));
        this.addSmelting(Block.ORE_DIAMOND.blockID, new ItemStack(Item.DIAMOND));
        this.addSmelting(Block.SAND.blockID, new ItemStack(Block.GLASS));
        this.addSmelting(Item.PORKCHOP_RAW.shiftedIndex, new ItemStack(Item.PORKCHOP_COOKED));
        this.addSmelting(Item.FISH_RAW.shiftedIndex, new ItemStack(Item.FISH_COOKED));
        this.addSmelting(Block.COBBLESTONE.blockID, new ItemStack(Block.STONE));
        this.addSmelting(Item.CLAY.shiftedIndex, new ItemStack(Item.BRICK));
        this.addSmelting(Block.CACTUS.blockID, new ItemStack(Item.DYE_POWDER, 1, 2));
        this.addSmelting(Block.WOOD.blockID, new ItemStack(Item.COAL, 1, 1));
    }

    public static FurnaceRecipes smelting() {
        return smeltingBase;
    }

    public void addSmelting(int var1, ItemStack var2) {
        this.smeltingList.put(var1, var2);
    }

    public ItemStack getSmeltingResult(int var1) {
        return this.smeltingList.get(var1);
    }

    public Map<Integer, ItemStack> getSmeltingList() {
        return this.smeltingList;
    }
}
