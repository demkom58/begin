package net.potion.stats;

import net.potion.achievement.AchievementList;
import net.potion.block.Block;
import net.potion.item.Item;
import net.potion.item.ItemStack;
import net.potion.item.crafting.CraftingManager;
import net.potion.item.crafting.FurnaceRecipes;
import net.potion.item.crafting.IRecipe;

import java.util.*;

public class StatList {
    public static List<StatBase> field1 = new ArrayList<>();
    public static List<StatBase> field2 = new ArrayList<>();
    public static List<StatCrafting> field3 = new ArrayList<>();
    public static List<StatCrafting> field4 = new ArrayList<>();

    public static StatBase[] field5;
    public static StatBase[] field6;
    public static StatBase[] field7;
    protected static Map<Integer, StatBase> id2statMap = new HashMap<>();
    public static StatBase startGameStat = (new StatBasic(1000, StatCollector.translateToLocal("stat.startGame"))).setClientSide().registerAchievement();
    public static StatBase createWorldStat = (new StatBasic(1001, StatCollector.translateToLocal("stat.createWorld"))).setClientSide().registerAchievement();
    public static StatBase loadWorldStat = (new StatBasic(1002, StatCollector.translateToLocal("stat.loadWorld"))).setClientSide().registerAchievement();
    public static StatBase joinMultiplayerStat = (new StatBasic(1003, StatCollector.translateToLocal("stat.joinMultiplayer"))).setClientSide().registerAchievement();
    public static StatBase leaveGameStat = (new StatBasic(1004, StatCollector.translateToLocal("stat.leaveGame"))).setClientSide().registerAchievement();
    public static StatBase minutesPlayedStat = (new StatBasic(1100, StatCollector.translateToLocal("stat.playOneMinute"), StatBase.statTime)).setClientSide().registerAchievement();
    public static StatBase distanceWalkedStat = (new StatBasic(2000, StatCollector.translateToLocal("stat.walkOneCm"), StatBase.statDistance)).setClientSide().registerAchievement();
    public static StatBase distanceSwumStat = (new StatBasic(2001, StatCollector.translateToLocal("stat.swimOneCm"), StatBase.statDistance)).setClientSide().registerAchievement();
    public static StatBase distanceFallenStat = (new StatBasic(2002, StatCollector.translateToLocal("stat.fallOneCm"), StatBase.statDistance)).setClientSide().registerAchievement();
    public static StatBase distanceClimbedStat = (new StatBasic(2003, StatCollector.translateToLocal("stat.climbOneCm"), StatBase.statDistance)).setClientSide().registerAchievement();
    public static StatBase distanceFlownStat = (new StatBasic(2004, StatCollector.translateToLocal("stat.flyOneCm"), StatBase.statDistance)).setClientSide().registerAchievement();
    public static StatBase distanceDoveStat = (new StatBasic(2005, StatCollector.translateToLocal("stat.diveOneCm"), StatBase.statDistance)).setClientSide().registerAchievement();
    public static StatBase distanceByMinecartStat = (new StatBasic(2006, StatCollector.translateToLocal("stat.minecartOneCm"), StatBase.statDistance)).setClientSide().registerAchievement();
    public static StatBase distanceByBoatStat = (new StatBasic(2007, StatCollector.translateToLocal("stat.boatOneCm"), StatBase.statDistance)).setClientSide().registerAchievement();
    public static StatBase distanceByPigStat = (new StatBasic(2008, StatCollector.translateToLocal("stat.pigOneCm"), StatBase.statDistance)).setClientSide().registerAchievement();
    public static StatBase jumpStat = (new StatBasic(2010, StatCollector.translateToLocal("stat.jump"))).setClientSide().registerAchievement();
    public static StatBase dropStat = (new StatBasic(2011, StatCollector.translateToLocal("stat.drop"))).setClientSide().registerAchievement();
    public static StatBase damageDealtStat = (new StatBasic(2020, StatCollector.translateToLocal("stat.damageDealt"))).registerAchievement();
    public static StatBase damageTakenStat = (new StatBasic(2021, StatCollector.translateToLocal("stat.damageTaken"))).registerAchievement();
    public static StatBase deathsStat = (new StatBasic(2022, StatCollector.translateToLocal("stat.deaths"))).registerAchievement();
    public static StatBase mobKillsStat = (new StatBasic(2023, StatCollector.translateToLocal("stat.mobKills"))).registerAchievement();
    public static StatBase playerKillsStat = (new StatBasic(2024, StatCollector.translateToLocal("stat.playerKills"))).registerAchievement();
    public static StatBase fishCaughtStat = (new StatBasic(2025, StatCollector.translateToLocal("stat.fishCaught"))).registerAchievement();
    public static StatBase[] mineBlockStatArray = method4("stat.mineBlock", 16777216);
    private static boolean field8 = false;
    private static boolean field9 = false;

    static {
        AchievementList.func_27374_a();
    }

    public static void init() {
    }

    public static void method1() {
        field6 = method5(field6, "stat.useItem", 16908288, 0, Block.BLOCKS_LIST.length);
        field7 = method6(field7, "stat.breakItem", 16973824, 0, Block.BLOCKS_LIST.length);
        field8 = true;
        method3();
    }

    public static void method2() {
        field6 = method5(field6, "stat.useItem", 16908288, Block.BLOCKS_LIST.length, 32000);
        field7 = method6(field7, "stat.breakItem", 16973824, Block.BLOCKS_LIST.length, 32000);
        field9 = true;
        method3();
    }

    public static void method3() {
        if (!field8 || !field9) {
            return;
        }

        Set<Integer> integers = new HashSet<>();
        for (IRecipe recipe : CraftingManager.getInstance().getRecipeList()) {
            integers.add(recipe.getRecipeResult().itemID);
        }

        for (ItemStack stack : FurnaceRecipes.smelting().getSmeltingList().values()) {
            integers.add(stack.itemID);
        }

        field5 = new StatBase[32000];

        for (Integer integer : integers) {
            if (Item.ITEMS_LIST[integer] != null) {
                String name = StatCollector.translateToLocalFormatted("stat.craftItem", Item.ITEMS_LIST[integer].getStatName());
                field5[integer] = (new StatCrafting(16842752 + integer, name, integer)).registerAchievement();
            }
        }

        replaceAllSimilarBlocks(field5);
    }

    private static StatBase[] method4(String var0, int var1) {
        StatCrafting[] stats = new StatCrafting[256];

        for (int i = 0; i < 256; ++i) {
            if (Block.BLOCKS_LIST[i] != null && Block.BLOCKS_LIST[i].isEnabledStats()) {
                String formatted = StatCollector.translateToLocalFormatted(var0, Block.BLOCKS_LIST[i].translateBlockName());
                stats[i] = (StatCrafting) (new StatCrafting(var1 + i, formatted, i)).registerAchievement();
                field4.add(stats[i]);
            }
        }

        replaceAllSimilarBlocks(stats);
        return stats;
    }

    private static StatBase[] method5(StatBase[] stats, String key, int var2, int from, int to) {
        if (stats == null)
            stats = new StatBase[32000];

        for (int i = from; i < to; ++i) {
            if (Item.ITEMS_LIST[i] != null) {
                String var6 = StatCollector.translateToLocalFormatted(key, Item.ITEMS_LIST[i].getStatName());
                stats[i] = new StatCrafting(var2 + i, var6, i).registerAchievement();
                if (i >= Block.BLOCKS_LIST.length) {
                    field3.add((StatCrafting) stats[i]);
                }
            }
        }

        replaceAllSimilarBlocks(stats);
        return stats;
    }

    private static StatBase[] method6(StatBase[] var0, String var1, int var2, int var3, int var4) {
        if (var0 == null)
            var0 = new StatBase[32000];

        for (int i = var3; i < var4; ++i) {
            if (Item.ITEMS_LIST[i] != null && Item.ITEMS_LIST[i].isDamagable()) {
                String var6 = StatCollector.translateToLocalFormatted(var1, Item.ITEMS_LIST[i].getStatName());
                var0[i] = (new StatCrafting(var2 + i, var6, i)).registerAchievement();
            }
        }

        replaceAllSimilarBlocks(var0);
        return var0;
    }

    private static void replaceAllSimilarBlocks(StatBase[] bases) {
        replaceSimilarBlocks(bases, Block.WATER_STILL.blockID, Block.WATER_MOVING.blockID);
        replaceSimilarBlocks(bases, Block.LAVA_STILL.blockID, Block.LAVA_STILL.blockID);
        replaceSimilarBlocks(bases, Block.PUMPKIN_LANTERN.blockID, Block.PUMPKIN.blockID);
        replaceSimilarBlocks(bases, Block.FURNACE_ACTIVE.blockID, Block.FURNACE.blockID);
        replaceSimilarBlocks(bases, Block.ORE_REDSTONE_GLOWING.blockID, Block.ORE_REDSTONE.blockID);
        replaceSimilarBlocks(bases, Block.REDSTONE_REPEATER_ACTIVE.blockID, Block.REDSTONE_REPEATER_IDLE.blockID);
        replaceSimilarBlocks(bases, Block.TORCH_REDSTONE_ACTIVE.blockID, Block.TORCH_REDSTONE_IDLE.blockID);
        replaceSimilarBlocks(bases, Block.MUSHROOM_RED.blockID, Block.MUSHROOM_BROWN.blockID);
        replaceSimilarBlocks(bases, Block.STAIR_DOUBLE.blockID, Block.STAIR_SINGLE.blockID);
        replaceSimilarBlocks(bases, Block.GRASS.blockID, Block.DIRT.blockID);
        replaceSimilarBlocks(bases, Block.FARMLAND.blockID, Block.DIRT.blockID);
    }

    private static void replaceSimilarBlocks(StatBase[] var0, int var1, int var2) {
        if (var0[var1] != null && var0[var2] == null) {
            var0[var2] = var0[var1];
        } else {
            field1.remove(var0[var1]);
            field4.remove(var0[var1]);
            field2.remove(var0[var1]);
            var0[var1] = var0[var2];
        }
    }

    public static StatBase getStat(int index) {
        return id2statMap.get(index);
    }
}
