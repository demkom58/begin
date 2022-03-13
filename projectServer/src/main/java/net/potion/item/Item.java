package net.potion.item;

import net.potion.block.Block;
import net.potion.entity.Entity;
import net.potion.entity.EntityLiving;
import net.potion.entity.player.EntityPlayer;
import net.potion.material.Material;
import net.potion.stats.StatCollector;
import net.potion.stats.StatList;
import net.potion.world.World;

import java.util.Random;

public class Item {
    public static final Item[] ITEMS_LIST = new Item[32000];
    public static final Item SHOVEL_IRON = new ItemSpade(0, MaterialGrade.IRON).setIconCoord(2, 5).setItemName("shovelIron");
    public static final Item PICKAXE_IRON = new ItemPickaxe(1, MaterialGrade.IRON).setIconCoord(2, 6).setItemName("pickaxeIron");
    public static final Item AXE_IRON = new ItemAxe(2, MaterialGrade.IRON).setIconCoord(2, 7).setItemName("hatchetIron");
    public static final Item FLINT_AND_STEEL = new ItemFlintAndSteel(3).setIconCoord(5, 0).setItemName("flintAndSteel");
    public static final Item APPLE_RED = new ItemFood(4, 4, false).setIconCoord(10, 0).setItemName("apple");
    public static final Item BOW = new ItemBow(5).setIconCoord(5, 1).setItemName("bow");
    public static final Item ARROW = new Item(6).setIconCoord(5, 2).setItemName("arrow");
    public static final Item COAL = new ItemCoal(7).setIconCoord(7, 0).setItemName("coal");
    public static final Item DIAMOND = new Item(8).setIconCoord(7, 3).setItemName("emerald");
    public static final Item INGOT_IRON = new Item(9).setIconCoord(7, 1).setItemName("ingotIron");
    public static final Item INGOT_GOLD = new Item(10).setIconCoord(7, 2).setItemName("ingotGold");
    public static final Item SWORD_IRON = new ItemSword(11, MaterialGrade.IRON).setIconCoord(2, 4).setItemName("swordIron");
    public static final Item SWORD_WOOD = new ItemSword(12, MaterialGrade.WOOD).setIconCoord(0, 4).setItemName("swordWood");
    public static final Item SHOVEL_WOOD = new ItemSpade(13, MaterialGrade.WOOD).setIconCoord(0, 5).setItemName("shovelWood");
    public static final Item PICKAXE_WOOD = new ItemPickaxe(14, MaterialGrade.WOOD).setIconCoord(0, 6).setItemName("pickaxeWood");
    public static final Item AXE_WOOD = new ItemAxe(15, MaterialGrade.WOOD).setIconCoord(0, 7).setItemName("hatchetWood");
    public static final Item SWORD_STONE = new ItemSword(16, MaterialGrade.STONE).setIconCoord(1, 4).setItemName("swordStone");
    public static final Item SHOVEL_STONE = new ItemSpade(17, MaterialGrade.STONE).setIconCoord(1, 5).setItemName("shovelStone");
    public static final Item PICKAXE_STONE = new ItemPickaxe(18, MaterialGrade.STONE).setIconCoord(1, 6).setItemName("pickaxeStone");
    public static final Item AXE_STONE = new ItemAxe(19, MaterialGrade.STONE).setIconCoord(1, 7).setItemName("hatchetStone");
    public static final Item SWORD_DIAMOND = new ItemSword(20, MaterialGrade.EMERALD).setIconCoord(3, 4).setItemName("swordDiamond");
    public static final Item SHOVEL_DIAMOND = new ItemSpade(21, MaterialGrade.EMERALD).setIconCoord(3, 5).setItemName("shovelDiamond");
    public static final Item PICKAXE_DIAMOND = new ItemPickaxe(22, MaterialGrade.EMERALD).setIconCoord(3, 6).setItemName("pickaxeDiamond");
    public static final Item AXE_DIAMOND = new ItemAxe(23, MaterialGrade.EMERALD).setIconCoord(3, 7).setItemName("hatchetDiamond");
    public static final Item STICK = new Item(24).setIconCoord(5, 3).setFull3D().setItemName("stick");
    public static final Item BOWL_EMPTY = new Item(25).setIconCoord(7, 4).setItemName("bowl");
    public static final Item BOWL_SOUP = new ItemSoup(26, 10).setIconCoord(8, 4).setItemName("mushroomStew");
    public static final Item SWORD_GOLD = new ItemSword(27, MaterialGrade.GOLD).setIconCoord(4, 4).setItemName("swordGold");
    public static final Item SHOVEL_GOLD = new ItemSpade(28, MaterialGrade.GOLD).setIconCoord(4, 5).setItemName("shovelGold");
    public static final Item PICKAXE_GOLD = new ItemPickaxe(29, MaterialGrade.GOLD).setIconCoord(4, 6).setItemName("pickaxeGold");
    public static final Item AXE_GOLD = new ItemAxe(30, MaterialGrade.GOLD).setIconCoord(4, 7).setItemName("hatchetGold");
    public static final Item SILK = new Item(31).setIconCoord(8, 0).setItemName("string");
    public static final Item FEATHER = new Item(32).setIconCoord(8, 1).setItemName("feather");
    public static final Item GUNPOWDER = new Item(33).setIconCoord(8, 2).setItemName("sulphur");
    public static final Item HOE_WOOD = new ItemHoe(34, MaterialGrade.WOOD).setIconCoord(0, 8).setItemName("hoeWood");
    public static final Item HOE_STONE = new ItemHoe(35, MaterialGrade.STONE).setIconCoord(1, 8).setItemName("hoeStone");
    public static final Item HOE_IRON = new ItemHoe(36, MaterialGrade.IRON).setIconCoord(2, 8).setItemName("hoeIron");
    public static final Item HOE_DIAMOND = new ItemHoe(37, MaterialGrade.EMERALD).setIconCoord(3, 8).setItemName("hoeDiamond");
    public static final Item HOE_GOLD = new ItemHoe(38, MaterialGrade.GOLD).setIconCoord(4, 8).setItemName("hoeGold");
    public static final Item SEEDS;
    public static final Item WHEAT = new Item(40).setIconCoord(9, 1).setItemName("wheat");
    public static final Item BREAD = new ItemFood(41, 5, false).setIconCoord(9, 2).setItemName("bread");
    public static final Item HELMET_LEATHER = new ItemArmor(42, 0, 0, 0).setIconCoord(0, 0).setItemName("helmetCloth");
    public static final Item CHESTPLATE_LEATHER = new ItemArmor(43, 0, 0, 1).setIconCoord(0, 1).setItemName("chestplateCloth");
    public static final Item LEGGINGS_LEATHER = new ItemArmor(44, 0, 0, 2).setIconCoord(0, 2).setItemName("leggingsCloth");
    public static final Item BOOTS_LEATHER = new ItemArmor(45, 0, 0, 3).setIconCoord(0, 3).setItemName("bootsCloth");
    public static final Item HELMET_CHAIN = new ItemArmor(46, 1, 1, 0).setIconCoord(1, 0).setItemName("helmetChain");
    public static final Item CHESTPLATE_CHAIN = new ItemArmor(47, 1, 1, 1).setIconCoord(1, 1).setItemName("chestplateChain");
    public static final Item LEGGINGS_CHAIN = new ItemArmor(48, 1, 1, 2).setIconCoord(1, 2).setItemName("leggingsChain");
    public static final Item BOOTS_CHAIN = new ItemArmor(49, 1, 1, 3).setIconCoord(1, 3).setItemName("bootsChain");
    public static final Item HELMET_IRON = new ItemArmor(50, 2, 2, 0).setIconCoord(2, 0).setItemName("helmetIron");
    public static final Item CHESTPLATE_IRON = new ItemArmor(51, 2, 2, 1).setIconCoord(2, 1).setItemName("chestplateIron");
    public static final Item LEGGINGS_IRON = new ItemArmor(52, 2, 2, 2).setIconCoord(2, 2).setItemName("leggingsIron");
    public static final Item BOOTS_IRON = new ItemArmor(53, 2, 2, 3).setIconCoord(2, 3).setItemName("bootsIron");
    public static final Item HELMET_DIAMOND = new ItemArmor(54, 3, 3, 0).setIconCoord(3, 0).setItemName("helmetDiamond");
    public static final Item CHESTPLATE_DIAMOND = new ItemArmor(55, 3, 3, 1).setIconCoord(3, 1).setItemName("chestplateDiamond");
    public static final Item LEGGINGS_DIAMOND = new ItemArmor(56, 3, 3, 2).setIconCoord(3, 2).setItemName("leggingsDiamond");
    public static final Item BOOTS_DIAMOND = new ItemArmor(57, 3, 3, 3).setIconCoord(3, 3).setItemName("bootsDiamond");
    public static final Item HELMET_GOLD = new ItemArmor(58, 1, 4, 0).setIconCoord(4, 0).setItemName("helmetGold");
    public static final Item CHESTPLATE_GOLD = new ItemArmor(59, 1, 4, 1).setIconCoord(4, 1).setItemName("chestplateGold");
    public static final Item LEGGINGS_GOLD = new ItemArmor(60, 1, 4, 2).setIconCoord(4, 2).setItemName("leggingsGold");
    public static final Item BOOTS_GOLD = new ItemArmor(61, 1, 4, 3).setIconCoord(4, 3).setItemName("bootsGold");
    public static final Item FLINT = new Item(62).setIconCoord(6, 0).setItemName("flint");
    public static final Item PORKCHOP_RAW = new ItemFood(63, 3, true).setIconCoord(7, 5).setItemName("porkchopRaw");
    public static final Item PORKCHOP_COOKED = new ItemFood(64, 8, true).setIconCoord(8, 5).setItemName("porkchopCooked");
    public static final Item PAINTING = new ItemPainting(65).setIconCoord(10, 1).setItemName("painting");
    public static final Item APPLE_GOLD = new ItemFood(66, 42, false).setIconCoord(11, 0).setItemName("appleGold");
    public static final Item SIGN = new ItemSign(67).setIconCoord(10, 2).setItemName("sign");
    public static final Item DOOR_WOOD = new ItemDoor(68, Material.WOOD).setIconCoord(11, 2).setItemName("doorWood");
    public static final Item BUCKET_EMPTY = new ItemBucket(69, 0).setIconCoord(10, 4).setItemName("bucket");
    public static final Item BUCKET_WATER;
    public static final Item BUCKET_LAVA;
    public static final Item MINECART = new ItemMinecart(72, 0).setIconCoord(7, 8).setItemName("minecart");
    public static final Item SADDLE = new ItemSaddle(73).setIconCoord(8, 6).setItemName("saddle");
    public static final Item DOOR_IRON = new ItemDoor(74, Material.IRON).setIconCoord(12, 2).setItemName("doorIron");
    public static final Item REDSTONE = new ItemRedstone(75).setIconCoord(8, 3).setItemName("redstone");
    public static final Item SNOWBALL = new ItemSnowball(76).setIconCoord(14, 0).setItemName("snowball");
    public static final Item BOAT = new ItemBoat(77).setIconCoord(8, 8).setItemName("boat");
    public static final Item LEATHER = new Item(78).setIconCoord(7, 6).setItemName("leather");
    public static final Item BUCKET_MILK = new ItemBucket(79, -1).setIconCoord(13, 4).setItemName("milk").setContainerItem(BUCKET_EMPTY);
    public static final Item BRICK = new Item(80).setIconCoord(6, 1).setItemName("brick");
    public static final Item CLAY = new Item(81).setIconCoord(9, 3).setItemName("clay");
    public static final Item REEDS = new ItemReed(82, Block.REEDS).setIconCoord(11, 1).setItemName("reeds");
    public static final Item PAPER = new Item(83).setIconCoord(10, 3).setItemName("paper");
    public static final Item BOOK = new Item(84).setIconCoord(11, 3).setItemName("book");
    public static final Item SLIMEBALL = new Item(85).setIconCoord(14, 1).setItemName("slimeball");
    public static final Item MINECART_CHEST = new ItemMinecart(86, 1).setIconCoord(7, 9).setItemName("minecartChest");
    public static final Item MINECART_FURNACE = new ItemMinecart(87, 2).setIconCoord(7, 10).setItemName("minecartFurnace");
    public static final Item EGG = new ItemEgg(88).setIconCoord(12, 0).setItemName("egg");
    public static final Item COMPASS = new Item(89).setIconCoord(6, 3).setItemName("compass");
    public static final Item FISHING_ROD = new ItemFishingRod(90).setIconCoord(5, 4).setItemName("fishingRod");
    public static final Item CLOCK = new Item(91).setIconCoord(6, 4).setItemName("clock");
    public static final Item LIGHT_STONE_DUST = new Item(92).setIconCoord(9, 4).setItemName("yellowDust");
    public static final Item FISH_RAW = new ItemFood(93, 2, false).setIconCoord(9, 5).setItemName("fishRaw");
    public static final Item FISH_COOKED = new ItemFood(94, 5, false).setIconCoord(10, 5).setItemName("fishCooked");
    public static final Item DYE_POWDER = new ItemDye(95).setIconCoord(14, 4).setItemName("dyePowder");
    public static final Item BONE = new Item(96).setIconCoord(12, 1).setItemName("bone").setFull3D();
    public static final Item SUGAR = new Item(97).setIconCoord(13, 0).setItemName("sugar").setFull3D();
    public static final Item CAKE = new ItemReed(98, Block.CAKE).setMaxStackSize(1).setIconCoord(13, 1).setItemName("cake");
    public static final Item BED = new ItemBed(99).setMaxStackSize(1).setIconCoord(13, 2).setItemName("bed");
    public static final Item REDSTONE_REPEATER = new ItemReed(100, Block.REDSTONE_REPEATER_IDLE).setIconCoord(6, 5).setItemName("diode");
    public static final Item COOKIE = new ItemCookie(101, 1, false, 8).setIconCoord(12, 5).setItemName("cookie");
    public static final ItemMap MAP = (ItemMap) new ItemMap(102).setIconCoord(12, 3).setItemName("map");
    public static final ItemShears SHEARS = (ItemShears) new ItemShears(103).setIconCoord(13, 5).setItemName("shears");
    public static final Item RECORD_13 = new ItemRecord(2000, "13").setIconCoord(0, 15).setItemName("record");
    public static final Item RECORD_CAT = new ItemRecord(2001, "cat").setIconCoord(1, 15).setItemName("record");
    protected static final Random ITEM_RAND = new Random();

    static {
        SEEDS = (new ItemSeeds(39, Block.CROPS.blockID)).setIconCoord(9, 0).setItemName("seeds");
        BUCKET_WATER = (new ItemBucket(70, Block.WATER_MOVING.blockID)).setIconCoord(11, 4).setItemName("bucketWater").setContainerItem(BUCKET_EMPTY);
        BUCKET_LAVA = (new ItemBucket(71, Block.LAVA_MOVING.blockID)).setIconCoord(12, 4).setItemName("bucketLava").setContainerItem(BUCKET_EMPTY);
        StatList.func_25086_b();
    }

    public final int shiftedIndex;
    protected int maxStackSize = 64;
    protected int iconIndex;
    protected boolean bFull3D = false;
    protected boolean hasSubtypes = false;
    private int maxDamage = 0;
    private Item containerItem = null;
    private String itemName;

    protected Item(int var1) {
        this.shiftedIndex = 256 + var1;
        if (ITEMS_LIST[256 + var1] != null) {
            System.out.println("CONFLICT @ " + var1);
        }

        ITEMS_LIST[256 + var1] = this;
    }

    public Item setIconIndex(int index) {
        this.iconIndex = index;
        return this;
    }

    public Item setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        return this;
    }

    public Item setIconCoord(int var1, int var2) {
        this.iconIndex = var1 + var2 * 16;
        return this;
    }

    public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7) {
        return false;
    }

    public float getStrVsBlock(ItemStack var1, Block var2) {
        return 1.0F;
    }

    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
        return var1;
    }

    public int getItemStackLimit() {
        return this.maxStackSize;
    }

    public int getMetadata(int var1) {
        return 0;
    }

    public boolean getHasSubtypes() {
        return this.hasSubtypes;
    }

    protected Item setHasSubtypes(boolean var1) {
        this.hasSubtypes = var1;
        return this;
    }

    public int getMaxDamage() {
        return this.maxDamage;
    }

    protected Item setMaxDamage(int var1) {
        this.maxDamage = var1;
        return this;
    }

    public boolean func_25005_e() {
        return this.maxDamage > 0 && !this.hasSubtypes;
    }

    public boolean hitEntity(ItemStack var1, EntityLiving var2, EntityLiving var3) {
        return false;
    }

    public boolean func_25007_a(ItemStack var1, int var2, int var3, int var4, int var5, EntityLiving var6) {
        return false;
    }

    public int getDamageVsEntity(Entity var1) {
        return 1;
    }

    public boolean canHarvestBlock(Block var1) {
        return false;
    }

    public void saddleEntity(ItemStack var1, EntityLiving var2) {
    }

    public Item setFull3D() {
        this.bFull3D = true;
        return this;
    }

    public String getItemName() {
        return this.itemName;
    }

    public Item setItemName(String var1) {
        this.itemName = "item." + var1;
        return this;
    }

    public Item getContainerItem() {
        return this.containerItem;
    }

    public Item setContainerItem(Item var1) {
        if (this.maxStackSize > 1) {
            throw new IllegalArgumentException("Max stack size must be 1 for items with crafting results");
        } else {
            this.containerItem = var1;
            return this;
        }
    }

    public boolean hasContainerItem() {
        return this.containerItem != null;
    }

    public String func_25006_i() {
        return StatCollector.translateToLocal(this.getItemName() + ".name");
    }

    public void func_28018_a(ItemStack var1, World var2, Entity var3, int var4, boolean var5) {
    }

    public void func_28020_c(ItemStack var1, World var2, EntityPlayer var3) {
    }

    public boolean func_28019_b() {
        return false;
    }
}
