package net.potion.item.crafting;

import net.potion.block.Block;
import net.potion.inventory.InventoryCrafting;
import net.potion.item.Item;
import net.potion.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingManager {
    private static final CraftingManager INSTANCE = new CraftingManager();
    private final List<IRecipe> recipes = new ArrayList<>();

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
        this.addRecipe(new ItemStack(Block.FENCE, 2), "###", "###", '#', Item.STICK);
        this.addRecipe(new ItemStack(Block.JUKEBOX, 1), "###", "#X#", "###", '#', Block.PLANKS, 'X', Item.DIAMOND);
        this.addRecipe(new ItemStack(Block.MUSIC_BLOCK, 1), "###", "#X#", "###", '#', Block.PLANKS, 'X', Item.REDSTONE);
        this.addRecipe(new ItemStack(Block.BOOKSHELF, 1), "###", "XXX", "###", '#', Block.PLANKS, 'X', Item.BOOK);
        this.addRecipe(new ItemStack(Block.BLOCK_SNOW, 1), "##", "##", '#', Item.SNOWBALL);
        this.addRecipe(new ItemStack(Block.BLOCK_CLAY, 1), "##", "##", '#', Item.CLAY);
        this.addRecipe(new ItemStack(Block.BRICK, 1), "##", "##", '#', Item.BRICK);
        this.addRecipe(new ItemStack(Block.GLOW_STONE, 1), "##", "##", '#', Item.LIGHT_STONE_DUST);
        this.addRecipe(new ItemStack(Block.CLOTH, 1), "##", "##", '#', Item.SILK);
        this.addRecipe(new ItemStack(Block.TNT, 1), "X#X", "#X#", "X#X", 'X', Item.GUNPOWDER, '#', Block.SAND);
        this.addRecipe(new ItemStack(Block.STAIR_SINGLE, 3, 3), "###", '#', Block.COBBLESTONE);
        this.addRecipe(new ItemStack(Block.STAIR_SINGLE, 3, 0), "###", '#', Block.STONE);
        this.addRecipe(new ItemStack(Block.STAIR_SINGLE, 3, 1), "###", '#', Block.SAND_STONE);
        this.addRecipe(new ItemStack(Block.STAIR_SINGLE, 3, 2), "###", '#', Block.PLANKS);
        this.addRecipe(new ItemStack(Block.LADDER, 2), "# #", "###", "# #", '#', Item.STICK);
        this.addRecipe(new ItemStack(Item.DOOR_WOOD, 1), "##", "##", "##", '#', Block.PLANKS);
        this.addRecipe(new ItemStack(Block.TRAPDOOR, 2), "###", "###", '#', Block.PLANKS);
        this.addRecipe(new ItemStack(Item.DOOR_IRON, 1), "##", "##", "##", '#', Item.INGOT_IRON);
        this.addRecipe(new ItemStack(Item.SIGN, 1), "###", "###", " X ", '#', Block.PLANKS, 'X', Item.STICK);
        this.addRecipe(new ItemStack(Item.CAKE, 1), "AAA", "BEB", "CCC", 'A', Item.BUCKET_MILK, 'B', Item.SUGAR, 'C', Item.WHEAT, 'E', Item.EGG);
        this.addRecipe(new ItemStack(Item.SUGAR, 1), "#", '#', Item.REEDS);
        this.addRecipe(new ItemStack(Block.PLANKS, 4), "#", '#', Block.WOOD);
        this.addRecipe(new ItemStack(Item.STICK, 4), "#", "#", '#', Block.PLANKS);
        this.addRecipe(new ItemStack(Block.TORCH_WOOD, 4), "X", "#", 'X', Item.COAL, '#', Item.STICK);
        this.addRecipe(new ItemStack(Block.TORCH_WOOD, 4), "X", "#", 'X', new ItemStack(Item.COAL, 1, 1), '#', Item.STICK);
        this.addRecipe(new ItemStack(Item.BOWL_EMPTY, 4), "# #", " # ", '#', Block.PLANKS);
        this.addRecipe(new ItemStack(Block.RAIL, 16), "X X", "X#X", "X X", 'X', Item.INGOT_IRON, '#', Item.STICK);
        this.addRecipe(new ItemStack(Block.RAIL_POWERED, 6), "X X", "X#X", "XRX", 'X', Item.INGOT_GOLD, 'R', Item.REDSTONE, '#', Item.STICK);
        this.addRecipe(new ItemStack(Block.RAIL_DETECTOR, 6), "X X", "X#X", "XRX", 'X', Item.INGOT_IRON, 'R', Item.REDSTONE, '#', Block.PRESSURE_PLATE_STONE);
        this.addRecipe(new ItemStack(Item.MINECART, 1), "# #", "###", '#', Item.INGOT_IRON);
        this.addRecipe(new ItemStack(Block.PUMPKIN_LANTERN, 1), "A", "B", 'A', Block.PUMPKIN, 'B', Block.TORCH_WOOD);
        this.addRecipe(new ItemStack(Item.MINECART_CHEST, 1), "A", "B", 'A', Block.CHEST, 'B', Item.MINECART);
        this.addRecipe(new ItemStack(Item.MINECART_FURNACE, 1), "A", "B", 'A', Block.FURNACE, 'B', Item.MINECART);
        this.addRecipe(new ItemStack(Item.BOAT, 1), "# #", "###", '#', Block.PLANKS);
        this.addRecipe(new ItemStack(Item.BUCKET_EMPTY, 1), "# #", " # ", '#', Item.INGOT_IRON);
        this.addRecipe(new ItemStack(Item.FLINT_AND_STEEL, 1), "A ", " B", 'A', Item.INGOT_IRON, 'B', Item.FLINT);
        this.addRecipe(new ItemStack(Item.BREAD, 1), "###", '#', Item.WHEAT);
        this.addRecipe(new ItemStack(Block.STAIR_COMPACT_PLANKS, 4), "#  ", "## ", "###", '#', Block.PLANKS);
        this.addRecipe(new ItemStack(Item.FISHING_ROD, 1), "  #", " #X", "# X", '#', Item.STICK, 'X', Item.SILK);
        this.addRecipe(new ItemStack(Block.STAIR_COMPACT_COBBLESTONE, 4), "#  ", "## ", "###", '#', Block.COBBLESTONE);
        this.addRecipe(new ItemStack(Item.PAINTING, 1), "###", "#X#", "###", '#', Item.STICK, 'X', Block.CLOTH);
        this.addRecipe(new ItemStack(Item.APPLE_GOLD, 1), "###", "#X#", "###", '#', Block.BLOCK_GOLD, 'X', Item.APPLE_RED);
        this.addRecipe(new ItemStack(Block.LEVER, 1), "X", "#", '#', Block.COBBLESTONE, 'X', Item.STICK);
        this.addRecipe(new ItemStack(Block.TORCH_REDSTONE_ACTIVE, 1), "X", "#", '#', Item.STICK, 'X', Item.REDSTONE);
        this.addRecipe(new ItemStack(Item.REDSTONE_REPEATER, 1), "#X#", "III", '#', Block.TORCH_REDSTONE_ACTIVE, 'X', Item.REDSTONE, 'I', Block.STONE);
        this.addRecipe(new ItemStack(Item.CLOCK, 1), " # ", "#X#", " # ", '#', Item.INGOT_GOLD, 'X', Item.REDSTONE);
        this.addRecipe(new ItemStack(Item.COMPASS, 1), " # ", "#X#", " # ", '#', Item.INGOT_IRON, 'X', Item.REDSTONE);
        this.addRecipe(new ItemStack(Item.MAP, 1), "###", "#X#", "###", '#', Item.PAPER, 'X', Item.COMPASS);
        this.addRecipe(new ItemStack(Block.BUTTON, 1), "#", "#", '#', Block.STONE);
        this.addRecipe(new ItemStack(Block.PRESSURE_PLATE_STONE, 1), "##", '#', Block.STONE);
        this.addRecipe(new ItemStack(Block.PRESSURE_PLATE_PLANKS, 1), "##", '#', Block.PLANKS);
        this.addRecipe(new ItemStack(Block.DISPENSER, 1), "###", "#X#", "#R#", '#', Block.COBBLESTONE, 'X', Item.BOW, 'R', Item.REDSTONE);
        this.addRecipe(new ItemStack(Block.PISTON_BASE, 1), "TTT", "#X#", "#R#", '#', Block.COBBLESTONE, 'X', Item.INGOT_IRON, 'R', Item.REDSTONE, 'T', Block.PLANKS);
        this.addRecipe(new ItemStack(Block.PISTON_STICKY_BASE, 1), "S", "P", 'S', Item.SLIMEBALL, 'P', Block.PISTON_BASE);
        this.addRecipe(new ItemStack(Item.BED, 1), "###", "XXX", '#', Block.CLOTH, 'X', Block.PLANKS);
        this.recipes.sort(new RecipeSorter(this));
        System.out.println(this.recipes.size() + " recipes");
    }

    public static CraftingManager getInstance() {
        return INSTANCE;
    }

    void addRecipe(ItemStack var1, Object... args) {
        StringBuilder var3 = new StringBuilder();
        int var4 = 0;
        int var5 = 0;
        int var6 = 0;
        if (args[var4] instanceof String[]) {
            String[] var11 = (String[]) args[var4++];

            for (int var8 = 0; var8 < var11.length; ++var8) {
                String var9 = var11[var8];
                ++var6;
                var5 = var9.length();
                var3.append(var9);
            }
        } else {
            while (args[var4] instanceof String) {
                String var7 = (String) args[var4++];
                ++var6;
                var5 = var7.length();
                var3.append(var7);
            }
        }

        Map<Character, ItemStack> var12;
        for (var12 = new HashMap<>(); var4 < args.length; var4 += 2) {
            Character var13 = (Character) args[var4];
            ItemStack var15 = null;
            if (args[var4 + 1] instanceof Item) {
                var15 = new ItemStack((Item) args[var4 + 1]);
            } else if (args[var4 + 1] instanceof Block) {
                var15 = new ItemStack((Block) args[var4 + 1], 1, -1);
            } else if (args[var4 + 1] instanceof ItemStack) {
                var15 = (ItemStack) args[var4 + 1];
            }

            var12.put(var13, var15);
        }

        ItemStack[] var14 = new ItemStack[var5 * var6];

        for (int var16 = 0; var16 < var5 * var6; ++var16) {
            char var10 = var3.charAt(var16);
            if (var12.containsKey(var10)) {
                var14[var16] = var12.get(var10).copy();
            } else {
                var14[var16] = null;
            }
        }

        this.recipes.add(new ShapedRecipes(var5, var6, var14, var1));
    }

    void addShapelessRecipe(ItemStack var1, Object... args) {
        List<ItemStack> stacks = new ArrayList<>();

        for (Object arg : args) {
            if (arg instanceof ItemStack argStack) {
                stacks.add(argStack.copy());
            } else if (arg instanceof Item item) {
                stacks.add(new ItemStack(item));
            } else if (arg instanceof Block block) {
                stacks.add(new ItemStack(block));
            } else {
                throw new RuntimeException("Invalid shapeless recipy!");
            }
        }

        this.recipes.add(new ShapelessRecipes(var1, stacks));
    }

    public ItemStack findMatchingRecipe(InventoryCrafting inv) {
        for (int i = 0; i < this.recipes.size(); ++i) {
            IRecipe recipe = this.recipes.get(i);
            if (recipe.matches(inv)) {
                return recipe.getCraftingResult(inv);
            }
        }

        return null;
    }

    public List<IRecipe> getRecipeList() {
        return this.recipes;
    }
}
