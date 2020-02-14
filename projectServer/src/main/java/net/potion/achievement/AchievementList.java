package net.potion.achievement;

import net.potion.block.Block;
import net.potion.item.Item;

import java.util.ArrayList;
import java.util.List;

public class AchievementList {
    public static int minDisplayColumn;
    public static int minDisplayRow;
    public static int maxDisplayColumn;
    public static int maxDisplayRow;
    public static List<Achievement> achievementList = new ArrayList<>();
    public static Achievement openInventory = (new Achievement(0, "openInventory", 0, 0, Item.BOOK, null)).func_27059_a().func_27061_c();
    public static Achievement mineWood = (new Achievement(1, "mineWood", 2, 1, Block.WOOD, openInventory)).func_27061_c();
    public static Achievement buildWorkBench = (new Achievement(2, "buildWorkBench", 4, -1, Block.WORKBENCH, mineWood)).func_27061_c();
    public static Achievement buildPickaxe = (new Achievement(3, "buildPickaxe", 4, 2, Item.PICKAXE_WOOD, buildWorkBench)).func_27061_c();
    public static Achievement buildFurnace = (new Achievement(4, "buildFurnace", 3, 4, Block.FURNACE_ACTIVE, buildPickaxe)).func_27061_c();
    public static Achievement acquireIron = (new Achievement(5, "acquireIron", 1, 4, Item.INGOT_IRON, buildFurnace)).func_27061_c();
    public static Achievement buildHoe = (new Achievement(6, "buildHoe", 2, -3, Item.HOE_WOOD, buildWorkBench)).func_27061_c();
    public static Achievement makeBread = (new Achievement(7, "makeBread", -1, -3, Item.BREAD, buildHoe)).func_27061_c();
    public static Achievement bakeCake = (new Achievement(8, "bakeCake", 0, -5, Item.CAKE, buildHoe)).func_27061_c();
    public static Achievement buildBetterPickaxe = (new Achievement(9, "buildBetterPickaxe", 6, 2, Item.PICKAXE_STONE, buildPickaxe)).func_27061_c();
    public static Achievement cookFish = (new Achievement(10, "cookFish", 2, 6, Item.FISH_COOKED, buildFurnace)).func_27061_c();
    public static Achievement onARail = (new Achievement(11, "onARail", 2, 3, Block.RAIL, acquireIron)).func_27060_b().func_27061_c();
    public static Achievement buildSword = (new Achievement(12, "buildSword", 6, -1, Item.SWORD_WOOD, buildWorkBench)).func_27061_c();
    public static Achievement killEnemy = (new Achievement(13, "killEnemy", 8, -1, Item.BONE, buildSword)).func_27061_c();
    public static Achievement killCow = (new Achievement(14, "killCow", 7, -3, Item.LEATHER, buildSword)).func_27061_c();
    public static Achievement flyPig = (new Achievement(15, "flyPig", 8, -4, Item.SADDLE, killCow)).func_27060_b().func_27061_c();

    static {
        System.out.println(achievementList.size() + " achievements");
    }

    public static void func_27374_a() {
    }
}
