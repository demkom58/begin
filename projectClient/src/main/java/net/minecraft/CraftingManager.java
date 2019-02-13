package net.minecraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CraftingManager {
    private static final CraftingManager instance = new CraftingManager();
    private List<IRecipe> recipes = new ArrayList<>();

    private CraftingManager() {
        (new RecipesTools()).addRecipes(this);
        (new RecipesWeapons()).addRecipes(this);
        (new RecipesIngots()).addRecipes(this);
        (new RecipesFood()).addRecipes(this);
        (new RecipesCrafting()).addRecipes(this);
        (new RecipesArmor()).addRecipes(this);
        (new RecipesDyes()).addRecipes(this);
        this.addRecipe(new ItemStack(Item.PAPER, 3), "###", '#', Item.REEDS);
        this.addRecipe(new ItemStack(Item.BOOK, 1), "#", "#", "#", '#', Item.PAPER);
        this.addRecipe(new ItemStack(Block.fence, 2), "###", "###", '#', Item.STICK);
        this.addRecipe(new ItemStack(Block.jukebox, 1), "###", "#X#", "###", '#', Block.planks, 'X', Item.DIAMOND);
        this.addRecipe(new ItemStack(Block.musicBlock, 1), "###", "#X#", "###", '#', Block.planks, 'X', Item.REDSTONE);
        this.addRecipe(new ItemStack(Block.bookShelf, 1), "###", "XXX", "###", '#', Block.planks, 'X', Item.BOOK);
        this.addRecipe(new ItemStack(Block.blockSnow, 1), "##", "##", '#', Item.SNOWBALL);
        this.addRecipe(new ItemStack(Block.blockClay, 1), "##", "##", '#', Item.CLAY);
        this.addRecipe(new ItemStack(Block.brick, 1), "##", "##", '#', Item.BRICK);
        this.addRecipe(new ItemStack(Block.glowStone, 1), "##", "##", '#', Item.LIGHT_STONE_DUST);
        this.addRecipe(new ItemStack(Block.cloth, 1), "##", "##", '#', Item.SILK);
        this.addRecipe(new ItemStack(Block.tnt, 1), "X#X", "#X#", "X#X", 'X', Item.GUNPOWDER, '#', Block.sand);
        this.addRecipe(new ItemStack(Block.stairSingle, 3, 3), "###", '#', Block.cobblestone);
        this.addRecipe(new ItemStack(Block.stairSingle, 3, 0), "###", '#', Block.stone);
        this.addRecipe(new ItemStack(Block.stairSingle, 3, 1), "###", '#', Block.sandStone);
        this.addRecipe(new ItemStack(Block.stairSingle, 3, 2), "###", '#', Block.planks);
        this.addRecipe(new ItemStack(Block.ladder, 2), "# #", "###", "# #", '#', Item.STICK);
        this.addRecipe(new ItemStack(Item.DOOR_WOOD, 1), "##", "##", "##", '#', Block.planks);
        this.addRecipe(new ItemStack(Block.trapdoor, 2), "###", "###", '#', Block.planks);
        this.addRecipe(new ItemStack(Item.DOOR_IRON, 1), "##", "##", "##", '#', Item.INGOT_IRON);
        this.addRecipe(new ItemStack(Item.SIGN, 1), "###", "###", " X ", '#', Block.planks, 'X', Item.STICK);
        this.addRecipe(new ItemStack(Item.CAKE, 1), "AAA", "BEB", "CCC", 'A', Item.BUCKET_MILK, 'B', Item.SUGAR, 'C', Item.WHEAT, 'E', Item.EGG);
        this.addRecipe(new ItemStack(Item.SUGAR, 1), "#", '#', Item.REEDS);
        this.addRecipe(new ItemStack(Block.planks, 4), "#", '#', Block.wood);
        this.addRecipe(new ItemStack(Item.STICK, 4), "#", "#", '#', Block.planks);
        this.addRecipe(new ItemStack(Block.torchWood, 4), "X", "#", 'X', Item.COAL, '#', Item.STICK);
        this.addRecipe(new ItemStack(Block.torchWood, 4), "X", "#", 'X', new ItemStack(Item.COAL, 1, 1), '#', Item.STICK);
        this.addRecipe(new ItemStack(Item.BOWL_EMPTY, 4), "# #", " # ", '#', Block.planks);
        this.addRecipe(new ItemStack(Block.rail, 16), "X X", "X#X", "X X", 'X', Item.INGOT_IRON, '#', Item.STICK);
        this.addRecipe(new ItemStack(Block.railPowered, 6), "X X", "X#X", "XRX", 'X', Item.INGOT_GOLD, 'R', Item.REDSTONE, '#', Item.STICK);
        this.addRecipe(new ItemStack(Block.railDetector, 6), "X X", "X#X", "XRX", 'X', Item.INGOT_IRON, 'R', Item.REDSTONE, '#', Block.pressurePlateStone);
        this.addRecipe(new ItemStack(Item.MINECART, 1), "# #", "###", '#', Item.INGOT_IRON);
        this.addRecipe(new ItemStack(Block.pumpkinLantern, 1), "A", "B", 'A', Block.pumpkin, 'B', Block.torchWood);
        this.addRecipe(new ItemStack(Item.MINECART_CHEST, 1), "A", "B", 'A', Block.chest, 'B', Item.MINECART);
        this.addRecipe(new ItemStack(Item.MINECART_FURNACE, 1), "A", "B", 'A', Block.stoneOvenIdle, 'B', Item.MINECART);
        this.addRecipe(new ItemStack(Item.BOAT, 1), "# #", "###", '#', Block.planks);
        this.addRecipe(new ItemStack(Item.BUCKET_EMPTY, 1), "# #", " # ", '#', Item.INGOT_IRON);
        this.addRecipe(new ItemStack(Item.FLINT_AND_STEEL, 1), "A ", " B", 'A', Item.INGOT_IRON, 'B', Item.FLINT);
        this.addRecipe(new ItemStack(Item.BREAD, 1), "###", '#', Item.WHEAT);
        this.addRecipe(new ItemStack(Block.stairCompactPlanks, 4), "#  ", "## ", "###", '#', Block.planks);
        this.addRecipe(new ItemStack(Item.FISHING_ROD, 1), "  #", " #X", "# X", '#', Item.STICK, 'X', Item.SILK);
        this.addRecipe(new ItemStack(Block.stairCompactCobblestone, 4), "#  ", "## ", "###", '#', Block.cobblestone);
        this.addRecipe(new ItemStack(Item.PAINTING, 1), "###", "#X#", "###", '#', Item.STICK, 'X', Block.cloth);
        this.addRecipe(new ItemStack(Item.APPLE_GOLD, 1), "###", "#X#", "###", '#', Block.blockGold, 'X', Item.APPLE_RED);
        this.addRecipe(new ItemStack(Block.lever, 1), "X", "#", '#', Block.cobblestone, 'X', Item.STICK);
        this.addRecipe(new ItemStack(Block.torchRedstoneActive, 1), "X", "#", '#', Item.STICK, 'X', Item.REDSTONE);
        this.addRecipe(new ItemStack(Item.REDSTONE_REPEATER, 1), "#X#", "III", '#', Block.torchRedstoneActive, 'X', Item.REDSTONE, 'I', Block.stone);
        this.addRecipe(new ItemStack(Item.CLOCK, 1), " # ", "#X#", " # ", '#', Item.INGOT_GOLD, 'X', Item.REDSTONE);
        this.addRecipe(new ItemStack(Item.COMPASS, 1), " # ", "#X#", " # ", '#', Item.INGOT_IRON, 'X', Item.REDSTONE);
        this.addRecipe(new ItemStack(Item.MAP, 1), "###", "#X#", "###", '#', Item.PAPER, 'X', Item.COMPASS);
        this.addRecipe(new ItemStack(Block.button, 1), "#", "#", '#', Block.stone);
        this.addRecipe(new ItemStack(Block.pressurePlateStone, 1), "##", '#', Block.stone);
        this.addRecipe(new ItemStack(Block.pressurePlatePlanks, 1), "##", '#', Block.planks);
        this.addRecipe(new ItemStack(Block.dispenser, 1), "###", "#X#", "#R#", '#', Block.cobblestone, 'X', Item.BOW, 'R', Item.REDSTONE);
        this.addRecipe(new ItemStack(Block.pistonBase, 1), "TTT", "#X#", "#R#", '#', Block.cobblestone, 'X', Item.INGOT_IRON, 'R', Item.REDSTONE, 'T', Block.planks);
        this.addRecipe(new ItemStack(Block.pistonStickyBase, 1), "S", "P", 'S', Item.SLIMEBALL, 'P', Block.pistonBase);
        this.addRecipe(new ItemStack(Item.BED, 1), "###", "XXX", '#', Block.cloth, 'X', Block.planks);
        Collections.sort(this.recipes, new RecipeSorter(this));
        System.out.println(this.recipes.size() + " recipes");
    }

    public static final CraftingManager getInstance() {
        return instance;
    }

    void addRecipe(ItemStack var1, Object... var2) {
        String var3 = "";
        int var4 = 0;
        int var5 = 0;
        int var6 = 0;
        if (var2[var4] instanceof String[]) {
            String[] var11 = (String[]) var2[var4++];

            for (int var8 = 0; var8 < var11.length; ++var8) {
                String var9 = var11[var8];
                ++var6;
                var5 = var9.length();
                var3 = var3 + var9;
            }
        } else {
            while (var2[var4] instanceof String) {
                String var7 = (String) var2[var4++];
                ++var6;
                var5 = var7.length();
                var3 = var3 + var7;
            }
        }

        HashMap var12;
        for (var12 = new HashMap(); var4 < var2.length; var4 += 2) {
            Character var13 = (Character) var2[var4];
            ItemStack var15 = null;
            if (var2[var4 + 1] instanceof Item) {
                var15 = new ItemStack((Item) var2[var4 + 1]);
            } else if (var2[var4 + 1] instanceof Block) {
                var15 = new ItemStack((Block) var2[var4 + 1], 1, -1);
            } else if (var2[var4 + 1] instanceof ItemStack) {
                var15 = (ItemStack) var2[var4 + 1];
            }

            var12.put(var13, var15);
        }

        ItemStack[] var14 = new ItemStack[var5 * var6];

        for (int var16 = 0; var16 < var5 * var6; ++var16) {
            char var10 = var3.charAt(var16);
            if (var12.containsKey(Character.valueOf(var10))) {
                var14[var16] = ((ItemStack) var12.get(Character.valueOf(var10))).copy();
            } else {
                var14[var16] = null;
            }
        }

        this.recipes.add(new ShapedRecipes(var5, var6, var14, var1));
    }

    void addShapelessRecipe(ItemStack var1, Object... var2) {
        ArrayList var3 = new ArrayList();

        for (Object var7 : var2) {
            if (var7 instanceof ItemStack) {
                var3.add(((ItemStack) var7).copy());
            } else if (var7 instanceof Item) {
                var3.add(new ItemStack((Item) var7));
            } else {
                if (!(var7 instanceof Block)) {
                    throw new RuntimeException("Invalid shapeless recipy!");
                }

                var3.add(new ItemStack((Block) var7));
            }
        }

        this.recipes.add(new ShapelessRecipes(var1, var3));
    }

    public ItemStack findMatchingRecipe(InventoryCrafting var1) {
        for (int var2 = 0; var2 < this.recipes.size(); ++var2) {
            IRecipe var3 = (IRecipe) this.recipes.get(var2);
            if (var3.matches(var1)) {
                return var3.getCraftingResult(var1);
            }
        }

        return null;
    }

    public List<IRecipe> getRecipeList() {
        return this.recipes;
    }
}
