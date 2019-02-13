package net.minecraft;

import java.util.HashMap;
import java.util.Map;

public class FurnaceRecipes {
    private static final FurnaceRecipes smeltingBase = new FurnaceRecipes();
    private Map<Integer, ItemStack> smeltingList = new HashMap<>();

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
