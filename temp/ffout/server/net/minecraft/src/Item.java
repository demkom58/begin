package net.minecraft.src;

import java.util.Random;

public class Item {
   protected static Random field_4154_b = new Random();
   public static Item[] field_176_c = new Item[32000];
   public static Item field_4153_d = (new ItemSpade(0, EnumToolMaterial.IRON)).func_20104_a(2, 5).func_20105_a("shovelIron");
   public static Item field_4152_e = (new ItemPickaxe(1, EnumToolMaterial.IRON)).func_20104_a(2, 6).func_20105_a("pickaxeIron");
   public static Item field_4151_f = (new ItemAxe(2, EnumToolMaterial.IRON)).func_20104_a(2, 7).func_20105_a("hatchetIron");
   public static Item field_4150_g = (new ItemFlintAndSteel(3)).func_20104_a(5, 0).func_20105_a("flintAndSteel");
   public static Item field_171_h = (new ItemFood(4, 4, false)).func_20104_a(10, 0).func_20105_a("apple");
   public static Item field_4149_i = (new ItemBow(5)).func_20104_a(5, 1).func_20105_a("bow");
   public static Item field_4148_j = (new Item(6)).func_20104_a(5, 2).func_20105_a("arrow");
   public static Item field_168_k = (new ItemCoal(7)).func_20104_a(7, 0).func_20105_a("coal");
   public static Item field_167_l = (new Item(8)).func_20104_a(7, 3).func_20105_a("emerald");
   public static Item field_166_m = (new Item(9)).func_20104_a(7, 1).func_20105_a("ingotIron");
   public static Item field_4147_n = (new Item(10)).func_20104_a(7, 2).func_20105_a("ingotGold");
   public static Item field_4146_o = (new ItemSword(11, EnumToolMaterial.IRON)).func_20104_a(2, 4).func_20105_a("swordIron");
   public static Item field_4145_p = (new ItemSword(12, EnumToolMaterial.WOOD)).func_20104_a(0, 4).func_20105_a("swordWood");
   public static Item field_4144_q = (new ItemSpade(13, EnumToolMaterial.WOOD)).func_20104_a(0, 5).func_20105_a("shovelWood");
   public static Item field_4143_r = (new ItemPickaxe(14, EnumToolMaterial.WOOD)).func_20104_a(0, 6).func_20105_a("pickaxeWood");
   public static Item field_4142_s = (new ItemAxe(15, EnumToolMaterial.WOOD)).func_20104_a(0, 7).func_20105_a("hatchetWood");
   public static Item field_4141_t = (new ItemSword(16, EnumToolMaterial.STONE)).func_20104_a(1, 4).func_20105_a("swordStone");
   public static Item field_4140_u = (new ItemSpade(17, EnumToolMaterial.STONE)).func_20104_a(1, 5).func_20105_a("shovelStone");
   public static Item field_4139_v = (new ItemPickaxe(18, EnumToolMaterial.STONE)).func_20104_a(1, 6).func_20105_a("pickaxeStone");
   public static Item field_4138_w = (new ItemAxe(19, EnumToolMaterial.STONE)).func_20104_a(1, 7).func_20105_a("hatchetStone");
   public static Item field_4137_x = (new ItemSword(20, EnumToolMaterial.EMERALD)).func_20104_a(3, 4).func_20105_a("swordDiamond");
   public static Item field_4136_y = (new ItemSpade(21, EnumToolMaterial.EMERALD)).func_20104_a(3, 5).func_20105_a("shovelDiamond");
   public static Item field_4135_z = (new ItemPickaxe(22, EnumToolMaterial.EMERALD)).func_20104_a(3, 6).func_20105_a("pickaxeDiamond");
   public static Item field_4175_A = (new ItemAxe(23, EnumToolMaterial.EMERALD)).func_20104_a(3, 7).func_20105_a("hatchetDiamond");
   public static Item field_209_B = (new Item(24)).func_20104_a(5, 3).func_4050_c().func_20105_a("stick");
   public static Item field_4173_C = (new Item(25)).func_20104_a(7, 4).func_20105_a("bowl");
   public static Item field_4172_D = (new ItemSoup(26, 10)).func_20104_a(8, 4).func_20105_a("mushroomStew");
   public static Item field_4171_E = (new ItemSword(27, EnumToolMaterial.GOLD)).func_20104_a(4, 4).func_20105_a("swordGold");
   public static Item field_4169_F = (new ItemSpade(28, EnumToolMaterial.GOLD)).func_20104_a(4, 5).func_20105_a("shovelGold");
   public static Item field_4168_G = (new ItemPickaxe(29, EnumToolMaterial.GOLD)).func_20104_a(4, 6).func_20105_a("pickaxeGold");
   public static Item field_4167_H = (new ItemAxe(30, EnumToolMaterial.GOLD)).func_20104_a(4, 7).func_20105_a("hatchetGold");
   public static Item field_196_I = (new Item(31)).func_20104_a(8, 0).func_20105_a("string");
   public static Item field_194_J = (new Item(32)).func_20104_a(8, 1).func_20105_a("feather");
   public static Item field_193_K = (new Item(33)).func_20104_a(8, 2).func_20105_a("sulphur");
   public static Item field_4166_L = (new ItemHoe(34, EnumToolMaterial.WOOD)).func_20104_a(0, 8).func_20105_a("hoeWood");
   public static Item field_4165_M = (new ItemHoe(35, EnumToolMaterial.STONE)).func_20104_a(1, 8).func_20105_a("hoeStone");
   public static Item field_4164_N = (new ItemHoe(36, EnumToolMaterial.IRON)).func_20104_a(2, 8).func_20105_a("hoeIron");
   public static Item field_4163_O = (new ItemHoe(37, EnumToolMaterial.EMERALD)).func_20104_a(3, 8).func_20105_a("hoeDiamond");
   public static Item field_4162_P = (new ItemHoe(38, EnumToolMaterial.GOLD)).func_20104_a(4, 8).func_20105_a("hoeGold");
   public static Item field_187_Q;
   public static Item field_186_R = (new Item(40)).func_20104_a(9, 1).func_20105_a("wheat");
   public static Item field_185_S = (new ItemFood(41, 5, false)).func_20104_a(9, 2).func_20105_a("bread");
   public static Item field_4161_T = (new ItemArmor(42, 0, 0, 0)).func_20104_a(0, 0).func_20105_a("helmetCloth");
   public static Item field_4160_U = (new ItemArmor(43, 0, 0, 1)).func_20104_a(0, 1).func_20105_a("chestplateCloth");
   public static Item field_4159_V = (new ItemArmor(44, 0, 0, 2)).func_20104_a(0, 2).func_20105_a("leggingsCloth");
   public static Item field_4158_W = (new ItemArmor(45, 0, 0, 3)).func_20104_a(0, 3).func_20105_a("bootsCloth");
   public static Item field_4157_X = (new ItemArmor(46, 1, 1, 0)).func_20104_a(1, 0).func_20105_a("helmetChain");
   public static Item field_4156_Y = (new ItemArmor(47, 1, 1, 1)).func_20104_a(1, 1).func_20105_a("chestplateChain");
   public static Item field_4155_Z = (new ItemArmor(48, 1, 1, 2)).func_20104_a(1, 2).func_20105_a("leggingsChain");
   public static Item field_4189_aa = (new ItemArmor(49, 1, 1, 3)).func_20104_a(1, 3).func_20105_a("bootsChain");
   public static Item field_4188_ab = (new ItemArmor(50, 2, 2, 0)).func_20104_a(2, 0).func_20105_a("helmetIron");
   public static Item field_4187_ac = (new ItemArmor(51, 2, 2, 1)).func_20104_a(2, 1).func_20105_a("chestplateIron");
   public static Item field_4186_ad = (new ItemArmor(52, 2, 2, 2)).func_20104_a(2, 2).func_20105_a("leggingsIron");
   public static Item field_4185_ae = (new ItemArmor(53, 2, 2, 3)).func_20104_a(2, 3).func_20105_a("bootsIron");
   public static Item field_4184_af = (new ItemArmor(54, 3, 3, 0)).func_20104_a(3, 0).func_20105_a("helmetDiamond");
   public static Item field_4183_ag = (new ItemArmor(55, 3, 3, 1)).func_20104_a(3, 1).func_20105_a("chestplateDiamond");
   public static Item field_4182_ah = (new ItemArmor(56, 3, 3, 2)).func_20104_a(3, 2).func_20105_a("leggingsDiamond");
   public static Item field_4181_ai = (new ItemArmor(57, 3, 3, 3)).func_20104_a(3, 3).func_20105_a("bootsDiamond");
   public static Item field_4180_aj = (new ItemArmor(58, 1, 4, 0)).func_20104_a(4, 0).func_20105_a("helmetGold");
   public static Item field_4179_ak = (new ItemArmor(59, 1, 4, 1)).func_20104_a(4, 1).func_20105_a("chestplateGold");
   public static Item field_4178_al = (new ItemArmor(60, 1, 4, 2)).func_20104_a(4, 2).func_20105_a("leggingsGold");
   public static Item field_4177_am = (new ItemArmor(61, 1, 4, 3)).func_20104_a(4, 3).func_20105_a("bootsGold");
   public static Item field_216_an = (new Item(62)).func_20104_a(6, 0).func_20105_a("flint");
   public static Item field_215_ao = (new ItemFood(63, 3, true)).func_20104_a(7, 5).func_20105_a("porkchopRaw");
   public static Item field_4176_ap = (new ItemFood(64, 8, true)).func_20104_a(8, 5).func_20105_a("porkchopCooked");
   public static Item field_213_aq = (new ItemPainting(65)).func_20104_a(10, 1).func_20105_a("painting");
   public static Item field_212_ar = (new ItemFood(66, 42, false)).func_20104_a(11, 0).func_20105_a("appleGold");
   public static Item field_4174_as = (new ItemSign(67)).func_20104_a(10, 2).func_20105_a("sign");
   public static Item field_208_at = (new ItemDoor(68, Material.field_524_c)).func_20104_a(11, 2).func_20105_a("doorWood");
   public static Item field_205_au = (new ItemBucket(69, 0)).func_20104_a(10, 4).func_20105_a("bucket");
   public static Item field_4170_av;
   public static Item field_201_aw;
   public static Item field_199_ax = (new ItemMinecart(72, 0)).func_20104_a(7, 8).func_20105_a("minecart");
   public static Item field_197_ay = (new ItemSaddle(73)).func_20104_a(8, 6).func_20105_a("saddle");
   public static Item field_195_az = (new ItemDoor(74, Material.field_522_e)).func_20104_a(12, 2).func_20105_a("doorIron");
   public static Item field_252_aA = (new ItemRedstone(75)).func_20104_a(8, 3).func_20105_a("redstone");
   public static Item field_251_aB = (new ItemSnowball(76)).func_20104_a(14, 0).func_20105_a("snowball");
   public static Item field_4205_aC = (new ItemBoat(77)).func_20104_a(8, 8).func_20105_a("boat");
   public static Item field_249_aD = (new Item(78)).func_20104_a(7, 6).func_20105_a("leather");
   public static Item field_4204_aE = (new ItemBucket(79, -1)).func_20104_a(13, 4).func_20105_a("milk").func_21093_a(field_205_au);
   public static Item field_4203_aF = (new Item(80)).func_20104_a(6, 1).func_20105_a("brick");
   public static Item field_246_aG = (new Item(81)).func_20104_a(9, 3).func_20105_a("clay");
   public static Item field_4202_aH = (new ItemReed(82, Block.field_620_aY)).func_20104_a(11, 1).func_20105_a("reeds");
   public static Item field_4201_aI = (new Item(83)).func_20104_a(10, 3).func_20105_a("paper");
   public static Item field_4200_aJ = (new Item(84)).func_20104_a(11, 3).func_20105_a("book");
   public static Item field_242_aK = (new Item(85)).func_20104_a(14, 1).func_20105_a("slimeball");
   public static Item field_4199_aL = (new ItemMinecart(86, 1)).func_20104_a(7, 9).func_20105_a("minecartChest");
   public static Item field_4198_aM = (new ItemMinecart(87, 2)).func_20104_a(7, 10).func_20105_a("minecartFurnace");
   public static Item field_239_aN = (new ItemEgg(88)).func_20104_a(12, 0).func_20105_a("egg");
   public static Item field_4197_aO = (new Item(89)).func_20104_a(6, 3).func_20105_a("compass");
   public static Item field_4196_aP = (new ItemFishingRod(90)).func_20104_a(5, 4).func_20105_a("fishingRod");
   public static Item field_4195_aQ = (new Item(91)).func_20104_a(6, 4).func_20105_a("clock");
   public static Item field_4194_aR = (new Item(92)).func_20104_a(9, 4).func_20105_a("yellowDust");
   public static Item field_4193_aS = (new ItemFood(93, 2, false)).func_20104_a(9, 5).func_20105_a("fishRaw");
   public static Item field_4192_aT = (new ItemFood(94, 5, false)).func_20104_a(10, 5).func_20105_a("fishCooked");
   public static Item field_21097_aU = (new ItemDye(95)).func_20104_a(14, 4).func_20105_a("dyePowder");
   public static Item field_21096_aV = (new Item(96)).func_20104_a(12, 1).func_20105_a("bone").func_4050_c();
   public static Item field_21095_aW = (new Item(97)).func_20104_a(13, 0).func_20105_a("sugar").func_4050_c();
   public static Item field_21098_aX = (new ItemReed(98, Block.field_21026_bg)).func_21086_c(1).func_20104_a(13, 1).func_20105_a("cake");
   public static Item field_22008_aY = (new ItemBed(99)).func_21086_c(1).func_20104_a(13, 2).func_20105_a("bed");
   public static Item field_22007_aZ = (new ItemReed(100, Block.field_22011_bh)).func_20104_a(6, 5).func_20105_a("diode");
   public static Item field_25008_ba = (new ItemCookie(101, 1, false, 8)).func_20104_a(12, 5).func_20105_a("cookie");
   public static ItemMap field_28021_bb = (ItemMap)(new ItemMap(102)).func_20104_a(12, 3).func_20105_a("map");
   public static ItemShears field_31022_bc = (ItemShears)(new ItemShears(103)).func_20104_a(13, 5).func_20105_a("shears");
   public static Item field_236_aQ = (new ItemRecord(2000, "13")).func_20104_a(0, 15).func_20105_a("record");
   public static Item field_4191_aV = (new ItemRecord(2001, "cat")).func_20104_a(1, 15).func_20105_a("record");
   public final int field_234_aS;
   protected int field_233_aT = 64;
   private int field_232_aU = 0;
   protected int field_6153_aZ;
   protected boolean field_230_aW = false;
   protected boolean field_21094_bf = false;
   private Item field_20900_a = null;
   private String field_275_a;

   protected Item(int var1) {
      this.field_234_aS = 256 + var1;
      if (field_176_c[256 + var1] != null) {
         System.out.println("CONFLICT @ " + var1);
      }

      field_176_c[256 + var1] = this;
   }

   public Item func_4049_a(int var1) {
      this.field_6153_aZ = var1;
      return this;
   }

   public Item func_21086_c(int var1) {
      this.field_233_aT = var1;
      return this;
   }

   public Item func_20104_a(int var1, int var2) {
      this.field_6153_aZ = var1 + var2 * 16;
      return this;
   }

   public boolean func_78_a(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7) {
      return false;
   }

   public float func_79_a(ItemStack var1, Block var2) {
      return 1.0F;
   }

   public ItemStack func_6152_a(ItemStack var1, World var2, EntityPlayer var3) {
      return var1;
   }

   public int func_81_a() {
      return this.field_233_aT;
   }

   public int func_21089_a(int var1) {
      return 0;
   }

   public boolean func_21092_c() {
      return this.field_21094_bf;
   }

   protected Item func_21091_a(boolean var1) {
      this.field_21094_bf = var1;
      return this;
   }

   public int func_77_b() {
      return this.field_232_aU;
   }

   protected Item func_21090_d(int var1) {
      this.field_232_aU = var1;
      return this;
   }

   public boolean func_25005_e() {
      return this.field_232_aU > 0 && !this.field_21094_bf;
   }

   public boolean func_9201_a(ItemStack var1, EntityLiving var2, EntityLiving var3) {
      return false;
   }

   public boolean func_25007_a(ItemStack var1, int var2, int var3, int var4, int var5, EntityLiving var6) {
      return false;
   }

   public int func_9203_a(Entity var1) {
      return 1;
   }

   public boolean func_80_a(Block var1) {
      return false;
   }

   public void func_9202_b(ItemStack var1, EntityLiving var2) {
   }

   public Item func_4050_c() {
      this.field_230_aW = true;
      return this;
   }

   public Item func_20105_a(String var1) {
      this.field_275_a = "item." + var1;
      return this;
   }

   public String func_20106_a() {
      return this.field_275_a;
   }

   public Item func_21093_a(Item var1) {
      if (this.field_233_aT > 1) {
         throw new IllegalArgumentException("Max stack size must be 1 for items with crafting results");
      } else {
         this.field_20900_a = var1;
         return this;
      }
   }

   public Item func_21087_f() {
      return this.field_20900_a;
   }

   public boolean func_21088_g() {
      return this.field_20900_a != null;
   }

   public String func_25006_i() {
      return StatCollector.func_25136_a(this.func_20106_a() + ".name");
   }

   public void func_28018_a(ItemStack var1, World var2, Entity var3, int var4, boolean var5) {
   }

   public void func_28020_c(ItemStack var1, World var2, EntityPlayer var3) {
   }

   public boolean func_28019_b() {
      return false;
   }

   static {
      field_187_Q = (new ItemSeeds(39, Block.field_644_aA.field_573_bc)).func_20104_a(9, 0).func_20105_a("seeds");
      field_4170_av = (new ItemBucket(70, Block.field_598_B.field_573_bc)).func_20104_a(11, 4).func_20105_a("bucketWater").func_21093_a(field_205_au);
      field_201_aw = (new ItemBucket(71, Block.field_594_D.field_573_bc)).func_20104_a(12, 4).func_20105_a("bucketLava").func_21093_a(field_205_au);
      StatList.func_25086_b();
   }
}
