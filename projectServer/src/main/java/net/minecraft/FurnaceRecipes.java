package net.minecraft;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;

import java.util.HashMap;
import java.util.Map;

public class FurnaceRecipes {
    private static final FurnaceRecipes SMELTING_BASE = new FurnaceRecipes();
    private Int2ObjectMap<ItemStack> smeltingList = new Int2ObjectRBTreeMap<>();

    private FurnaceRecipes() {
        this.addSmelting(Block.oreIron.blockID, new ItemStack(Item.ingotIron));
        this.addSmelting(Block.oreGold.blockID, new ItemStack(Item.ingotGold));
        this.addSmelting(Block.oreDiamond.blockID, new ItemStack(Item.diamond));
        this.addSmelting(Block.sand.blockID, new ItemStack(Block.glass));
        this.addSmelting(Item.porkRaw.shiftedIndex, new ItemStack(Item.porkCooked));
        this.addSmelting(Item.fishRaw.shiftedIndex, new ItemStack(Item.fishCooked));
        this.addSmelting(Block.cobblestone.blockID, new ItemStack(Block.stone));
        this.addSmelting(Item.clay.shiftedIndex, new ItemStack(Item.brick));
        this.addSmelting(Block.cactus.blockID, new ItemStack(Item.dyePowder, 1, 2));
        this.addSmelting(Block.wood.blockID, new ItemStack(Item.coal, 1, 1));
    }

    public static FurnaceRecipes smelting() {
        return SMELTING_BASE;
    }

    public void addSmelting(int id, ItemStack result) {
        this.smeltingList.put(id, result);
    }

    public ItemStack getSmeltingResult(int id) {
        return this.smeltingList.get(id);
    }

    public Map<Integer, ItemStack> getSmeltingList() {
        return this.smeltingList;
    }
}
