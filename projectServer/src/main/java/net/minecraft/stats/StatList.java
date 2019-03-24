package net.minecraft.stats;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.achievement.AchievementList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.*;

public class StatList {
    public static List<StatBase> field_25123_a = new ArrayList<>();
    public static List<StatBase> field_25122_b = new ArrayList<>();
    public static List<StatBase> field_25121_c = new ArrayList<>();
    public static List<StatBase> field_25120_d = new ArrayList<>();
    public static StatBase[] field_25093_z;
    public static StatBase[] field_25107_A;
    public static StatBase[] field_25105_B;
    protected static Int2ObjectMap<StatBase> field_25104_C = new Int2ObjectRBTreeMap<>();
    public static StatBase field_25119_e = (new StatBasic(1000, StatCollector.translateToLocal("stat.startGame"))).func_27052_e().func_27053_d();
    public static StatBase field_25118_f = (new StatBasic(1001, StatCollector.translateToLocal("stat.createWorld"))).func_27052_e().func_27053_d();
    public static StatBase field_25117_g = (new StatBasic(1002, StatCollector.translateToLocal("stat.loadWorld"))).func_27052_e().func_27053_d();
    public static StatBase field_25116_h = (new StatBasic(1003, StatCollector.translateToLocal("stat.joinMultiplayer"))).func_27052_e().func_27053_d();
    public static StatBase field_25115_i = (new StatBasic(1004, StatCollector.translateToLocal("stat.leaveGame"))).func_27052_e().func_27053_d();
    public static StatBase field_25114_j = (new StatBasic(1100, StatCollector.translateToLocal("stat.playOneMinute"), StatBase.field_27055_j)).func_27052_e().func_27053_d();
    public static StatBase field_25113_k = (new StatBasic(2000, StatCollector.translateToLocal("stat.walkOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
    public static StatBase field_25112_l = (new StatBasic(2001, StatCollector.translateToLocal("stat.swimOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
    public static StatBase field_25111_m = (new StatBasic(2002, StatCollector.translateToLocal("stat.fallOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
    public static StatBase field_25110_n = (new StatBasic(2003, StatCollector.translateToLocal("stat.climbOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
    public static StatBase field_25109_o = (new StatBasic(2004, StatCollector.translateToLocal("stat.flyOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
    public static StatBase field_25108_p = (new StatBasic(2005, StatCollector.translateToLocal("stat.diveOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
    public static StatBase field_27095_r = (new StatBasic(2006, StatCollector.translateToLocal("stat.minecartOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
    public static StatBase field_27094_s = (new StatBasic(2007, StatCollector.translateToLocal("stat.boatOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
    public static StatBase field_27093_t = (new StatBasic(2008, StatCollector.translateToLocal("stat.pigOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
    public static StatBase field_25106_q = (new StatBasic(2010, StatCollector.translateToLocal("stat.jump"))).func_27052_e().func_27053_d();
    public static StatBase field_25103_r = (new StatBasic(2011, StatCollector.translateToLocal("stat.drop"))).func_27052_e().func_27053_d();
    public static StatBase field_25102_s = (new StatBasic(2020, StatCollector.translateToLocal("stat.damageDealt"))).func_27053_d();
    public static StatBase field_25100_t = (new StatBasic(2021, StatCollector.translateToLocal("stat.damageTaken"))).func_27053_d();
    public static StatBase field_25098_u = (new StatBasic(2022, StatCollector.translateToLocal("stat.deaths"))).func_27053_d();
    public static StatBase field_25097_v = (new StatBasic(2023, StatCollector.translateToLocal("stat.mobKills"))).func_27053_d();
    public static StatBase field_25096_w = (new StatBasic(2024, StatCollector.translateToLocal("stat.playerKills"))).func_27053_d();
    public static StatBase fishCaughtStat = (new StatBasic(2025, StatCollector.translateToLocal("stat.fishCaught"))).func_27053_d();
    public static StatBase[] mineBlockStatArray = func_25089_a("stat.mineBlock", 16777216);
    private static boolean field_25101_D = false;
    private static boolean field_25099_E = false;

    static {
        AchievementList.func_27374_a();
    }

    public static void func_27092_a() {
    }

    public static void func_25088_a() {
        field_25107_A = func_25090_a(field_25107_A, "stat.useItem", 16908288, 0, Block.BLOCKS_LIST.length);
        field_25105_B = func_25087_b(field_25105_B, "stat.breakItem", 16973824, 0, Block.BLOCKS_LIST.length);
        field_25101_D = true;
        func_25091_c();
    }

    public static void func_25086_b() {
        field_25107_A = func_25090_a(field_25107_A, "stat.useItem", 16908288, Block.BLOCKS_LIST.length, 32000);
        field_25105_B = func_25087_b(field_25105_B, "stat.breakItem", 16973824, Block.BLOCKS_LIST.length, 32000);
        field_25099_E = true;
        func_25091_c();
    }

    public static void func_25091_c() {
        if (field_25101_D && field_25099_E) {
            Set<Integer> var0 = new HashSet<>();

            for (IRecipe var2 : CraftingManager.getInstance().getRecipeList()) {
                var0.add(var2.getRecipeOutput().itemID);
            }

            for (ItemStack var6 : FurnaceRecipes.smelting().getSmeltingList().values()) {
                var0.add(var6.itemID);
            }

            field_25093_z = new StatBase[32000];

            for (Integer var7 : var0) {
                if (Item.ITEMS_LIST[var7] != null) {
                    String var3 = StatCollector.translateToLocalFormatted("stat.craftItem", Item.ITEMS_LIST[var7].func_25006_i());
                    field_25093_z[var7] = (new StatCrafting(16842752 + var7, var3, var7)).func_27053_d();
                }
            }

            replaceAllSimilarBlocks(field_25093_z);
        }
    }

    private static StatBase[] func_25089_a(String var0, int var1) {
        StatBase[] var2 = new StatBase[256];

        for (int var3 = 0; var3 < 256; ++var3) {
            if (Block.BLOCKS_LIST[var3] != null && Block.BLOCKS_LIST[var3].getEnableStats()) {
                String var4 = StatCollector.translateToLocalFormatted(var0, Block.BLOCKS_LIST[var3].getNameLocalizedForStats());
                var2[var3] = (new StatCrafting(var1 + var3, var4, var3)).func_27053_d();
                field_25120_d.add(var2[var3]);
            }
        }

        replaceAllSimilarBlocks(var2);
        return var2;
    }

    private static StatBase[] func_25090_a(StatBase[] var0, String var1, int var2, int var3, int var4) {
        if (var0 == null) {
            var0 = new StatBase[32000];
        }

        for (int var5 = var3; var5 < var4; ++var5) {
            if (Item.ITEMS_LIST[var5] != null) {
                String var6 = StatCollector.translateToLocalFormatted(var1, Item.ITEMS_LIST[var5].func_25006_i());
                var0[var5] = (new StatCrafting(var2 + var5, var6, var5)).func_27053_d();
                if (var5 >= Block.BLOCKS_LIST.length) {
                    field_25121_c.add(var0[var5]);
                }
            }
        }

        replaceAllSimilarBlocks(var0);
        return var0;
    }

    private static StatBase[] func_25087_b(StatBase[] var0, String var1, int var2, int var3, int var4) {
        if (var0 == null) {
            var0 = new StatBase[32000];
        }

        for (int var5 = var3; var5 < var4; ++var5) {
            if (Item.ITEMS_LIST[var5] != null && Item.ITEMS_LIST[var5].func_25005_e()) {
                String var6 = StatCollector.translateToLocalFormatted(var1, Item.ITEMS_LIST[var5].func_25006_i());
                var0[var5] = (new StatCrafting(var2 + var5, var6, var5)).func_27053_d();
            }
        }

        replaceAllSimilarBlocks(var0);
        return var0;
    }

    private static void replaceAllSimilarBlocks(StatBase[] var0) {
        replaceSimilarBlocks(var0, Block.WATER_STILL.blockID, Block.WATER_MOVING.blockID);
        replaceSimilarBlocks(var0, Block.LAVA_STILL.blockID, Block.LAVA_STILL.blockID);
        replaceSimilarBlocks(var0, Block.PUMPKIN_LANTERN.blockID, Block.PUMPKIN.blockID);
        replaceSimilarBlocks(var0, Block.FURNACE_ACTIVE.blockID, Block.FURNACE.blockID);
        replaceSimilarBlocks(var0, Block.ORE_REDSTONE_GLOWING.blockID, Block.ORE_REDSTONE.blockID);
        replaceSimilarBlocks(var0, Block.REDSTONE_REPEATER_ACTIVE.blockID, Block.REDSTONE_REPEATER_IDLE.blockID);
        replaceSimilarBlocks(var0, Block.TORCH_REDSTONE_ACTIVE.blockID, Block.TORCH_REDSTONE_IDLE.blockID);
        replaceSimilarBlocks(var0, Block.MUSHROOM_RED.blockID, Block.MUSHROOM_BROWN.blockID);
        replaceSimilarBlocks(var0, Block.STAIR_DOUBLE.blockID, Block.STAIR_SINGLE.blockID);
        replaceSimilarBlocks(var0, Block.GRASS.blockID, Block.DIRT.blockID);
        replaceSimilarBlocks(var0, Block.FARMLAND.blockID, Block.DIRT.blockID);
    }

    private static void replaceSimilarBlocks(StatBase[] var0, int var1, int var2) {
        if (var0[var1] != null && var0[var2] == null) {
            var0[var2] = var0[var1];
        } else {
            field_25123_a.remove(var0[var1]);
            field_25120_d.remove(var0[var1]);
            field_25122_b.remove(var0[var1]);
            var0[var1] = var0[var2];
        }
    }
}
