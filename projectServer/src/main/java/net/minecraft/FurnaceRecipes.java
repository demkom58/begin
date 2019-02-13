package net.minecraft;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;

import java.util.Map;

public class FurnaceRecipes {
    private static final FurnaceRecipes SMELTING_BASE = new FurnaceRecipes();
    private Int2ObjectMap<ItemStack> smeltingList = new Int2ObjectRBTreeMap<>();

    private FurnaceRecipes() {
        this.addSmelting(Block.oreIron.blockID, new ItemStack(Item.INGOT_IRON));
        this.addSmelting(Block.oreGold.blockID, new ItemStack(Item.INGOT_GOLD));
        this.addSmelting(Block.oreDiamond.blockID, new ItemStack(Item.DIAMOND));
        this.addSmelting(Block.sand.blockID, new ItemStack(Block.glass));
        this.addSmelting(Item.PORKCHOP_RAW.shiftedIndex, new ItemStack(Item.PORKCHOP_COOKED));
        this.addSmelting(Item.FISH_RAW.shiftedIndex, new ItemStack(Item.FISH_COOKED));
        this.addSmelting(Block.cobblestone.blockID, new ItemStack(Block.stone));
        this.addSmelting(Item.CLAY.shiftedIndex, new ItemStack(Item.BRICK));
        this.addSmelting(Block.cactus.blockID, new ItemStack(Item.DYE_POWDER, 1, 2));
        this.addSmelting(Block.wood.blockID, new ItemStack(Item.COAL, 1, 1));
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
