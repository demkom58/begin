package net.minecraft.block;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumMobType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.material.Material;
import net.minecraft.stats.StatCollector;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import util.Vec3D;

import java.util.List;
import java.util.Random;

public class Block {
    public static final StepSound SOUND_POWDER_FOOTSTEP = new StepSound("stone", 1.0F, 1.0F);
    public static final StepSound SOUND_WOOD_FOOTSTEP = new StepSound("wood", 1.0F, 1.0F);
    public static final StepSound SOUND_GRAVEL_FOOTSTEP = new StepSound("gravel", 1.0F, 1.0F);
    public static final StepSound SOUND_GRASS_FOOTSTEP = new StepSound("grass", 1.0F, 1.0F);
    public static final StepSound SOUND_STONE_FOOTSTEP = new StepSound("stone", 1.0F, 1.0F);
    public static final StepSound SOUND_METAL_FOOTSTEP = new StepSound("stone", 1.0F, 1.5F);
    public static final StepSound SOUND_GLASS_FOOTSTEP = new StepSoundStone("stone", 1.0F, 1.0F);
    public static final StepSound SOUND_CLOTH_FOOTSTEP = new StepSound("cloth", 1.0F, 1.0F);
    public static final StepSound SOUND_SAND_FOOTSTEP = new StepSoundSand("sand", 1.0F, 1.0F);

    public static final Block[] BLOCKS_LIST = new Block[256];
    public static final boolean[] TICK_ON_LOAD = new boolean[256];
    public static final boolean[] OPAQUE_CUBE_LOOKUP = new boolean[256];
    public static final boolean[] IS_BLOCK_CONTAINER = new boolean[256];
    public static final int[] LIGHT_OPACITY = new int[256];
    public static final boolean[] CAN_BLOCK_GRASS = new boolean[256];
    public static final int[] LIGHT_VALUE = new int[256];
    public static final boolean[] REQUIRES_SELF_NOTIFY = new boolean[256];

    public static final Block STONE = new BlockStone(1, 1).setHardness(1.5F).setResistance(10.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("stone");
    public static final BlockGrass GRASS = (BlockGrass) new BlockGrass(2).setHardness(0.6F).setStepSound(SOUND_GRASS_FOOTSTEP).setBlockName("grass");
    public static final Block DIRT = new BlockDirt(3, 2).setHardness(0.5F).setStepSound(SOUND_GRAVEL_FOOTSTEP).setBlockName("dirt");
    public static final Block COBBLESTONE = new Block(4, 16, Material.ROCK).setHardness(2.0F).setResistance(10.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("stonebrick");
    public static final Block PLANKS = new Block(5, 4, Material.WOOD).setHardness(2.0F).setResistance(5.0F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("wood").disableNeighborNotifyOnMetadataChange();
    public static final Block SAPLING = new BlockSapling(6, 15).setHardness(0.0F).setStepSound(SOUND_GRASS_FOOTSTEP).setBlockName("sapling").disableNeighborNotifyOnMetadataChange();
    public static final Block BEDROCK = new Block(7, 17, Material.ROCK).setBlockUnbreakable().setResistance(6000000.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("bedrock").disableStats();
    public static final Block WATER_MOVING = new BlockFlowing(8, Material.WATER).setHardness(100.0F).setLightOpacity(3).setBlockName("water").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block WATER_STILL = new BlockStationary(9, Material.WATER).setHardness(100.0F).setLightOpacity(3).setBlockName("water").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block LAVA_MOVING = new BlockFlowing(10, Material.LAVA).setHardness(0.0F).setLightValue(1.0F).setLightOpacity(255).setBlockName("lava").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block LAVA_STILL = new BlockStationary(11, Material.LAVA).setHardness(100.0F).setLightValue(1.0F).setLightOpacity(255).setBlockName("lava").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block SAND = new BlockSand(12, 18).setHardness(0.5F).setStepSound(SOUND_SAND_FOOTSTEP).setBlockName("sand");
    public static final Block GRAVEL = new BlockGravel(13, 19).setHardness(0.6F).setStepSound(SOUND_GRAVEL_FOOTSTEP).setBlockName("gravel");
    public static final Block ORE_GOLD = new BlockOre(14, 32).setHardness(3.0F).setResistance(5.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("oreGold");
    public static final Block ORE_IRON = new BlockOre(15, 33).setHardness(3.0F).setResistance(5.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("oreIron");
    public static final Block ORE_COAL = new BlockOre(16, 34).setHardness(3.0F).setResistance(5.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("oreCoal");
    public static final Block WOOD = new BlockLog(17).setHardness(2.0F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("log").disableNeighborNotifyOnMetadataChange();
    public static final BlockLeaves LEAVES = (BlockLeaves) new BlockLeaves(18, 52).setHardness(0.2F).setLightOpacity(1).setStepSound(SOUND_GRASS_FOOTSTEP).setBlockName("leaves").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block SPONGE = new BlockSponge(19).setHardness(0.6F).setStepSound(SOUND_GRASS_FOOTSTEP).setBlockName("sponge");
    public static final Block GLASS = new BlockGlass(20, 49, Material.GLASS, false).setHardness(0.3F).setStepSound(SOUND_GLASS_FOOTSTEP).setBlockName("glass");
    public static final Block ORE_LAPIS = new BlockOre(21, 160).setHardness(3.0F).setResistance(5.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("oreLapis");
    public static final Block BLOCK_LAPIS = new Block(22, 144, Material.ROCK).setHardness(3.0F).setResistance(5.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("blockLapis");
    public static final Block DISPENSER = new BlockDispenser(23).setHardness(3.5F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("dispenser").disableNeighborNotifyOnMetadataChange();
    public static final Block SAND_STONE = new BlockSandStone(24).setStepSound(SOUND_STONE_FOOTSTEP).setHardness(0.8F).setBlockName("sandStone");
    public static final Block MUSIC_BLOCK = new BlockNote(25).setHardness(0.8F).setBlockName("musicBlock").disableNeighborNotifyOnMetadataChange();
    public static final Block BED = new BlockBed(26).setHardness(0.2F).setBlockName("bed").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block RAIL_POWERED = new BlockRail(27, 179, true).setHardness(0.7F).setStepSound(SOUND_METAL_FOOTSTEP).setBlockName("goldenRail").disableNeighborNotifyOnMetadataChange();
    public static final Block RAIL_DETECTOR = new BlockDetectorRail(28, 195).setHardness(0.7F).setStepSound(SOUND_METAL_FOOTSTEP).setBlockName("detectorRail").disableNeighborNotifyOnMetadataChange();
    public static final Block PISTON_STICKY_BASE = new BlockPistonBase(29, 106, true).setBlockName("pistonStickyBase").disableNeighborNotifyOnMetadataChange();
    public static final Block WEB = new BlockWeb(30, 11).setLightOpacity(1).setHardness(4.0F).setBlockName("web");
    public static final BlockTallGrass TALLGRASS = (BlockTallGrass) new BlockTallGrass(31, 39).setHardness(0.0F).setStepSound(SOUND_GRASS_FOOTSTEP).setBlockName("tallgrass");
    public static final BlockDeadBush DEADBUSH = (BlockDeadBush) new BlockDeadBush(32, 55).setHardness(0.0F).setStepSound(SOUND_GRASS_FOOTSTEP).setBlockName("deadbush");
    public static final Block PISTON_BASE = new BlockPistonBase(33, 107, false).setBlockName("pistonBase").disableNeighborNotifyOnMetadataChange();
    public static final BlockPistonExtension PISTON_EXTENSION = (BlockPistonExtension) new BlockPistonExtension(34, 107).disableNeighborNotifyOnMetadataChange();
    public static final Block CLOTH = new BlockCloth().setHardness(0.8F).setStepSound(SOUND_CLOTH_FOOTSTEP).setBlockName("cloth").disableNeighborNotifyOnMetadataChange();
    public static final BlockPistonMoving PISTON_MOVING = new BlockPistonMoving(36);
    public static final BlockFlower PLANT_YELLOW = (BlockFlower) new BlockFlower(37, 13).setHardness(0.0F).setStepSound(SOUND_GRASS_FOOTSTEP).setBlockName("flower");
    public static final BlockFlower PLANT_RED = (BlockFlower) new BlockFlower(38, 12).setHardness(0.0F).setStepSound(SOUND_GRASS_FOOTSTEP).setBlockName("rose");
    public static final BlockFlower MUSHROOM_BROWN = (BlockFlower) new BlockMushroom(39, 29).setHardness(0.0F).setStepSound(SOUND_GRASS_FOOTSTEP).setLightValue(0.125F).setBlockName("mushroom");
    public static final BlockFlower MUSHROOM_RED = (BlockFlower) new BlockMushroom(40, 28).setHardness(0.0F).setStepSound(SOUND_GRASS_FOOTSTEP).setBlockName("mushroom");
    public static final Block BLOCK_GOLD = new BlockOreStorage(41, 23).setHardness(3.0F).setResistance(10.0F).setStepSound(SOUND_METAL_FOOTSTEP).setBlockName("blockGold");
    public static final Block BLOCK_IRON = new BlockOreStorage(42, 22).setHardness(5.0F).setResistance(10.0F).setStepSound(SOUND_METAL_FOOTSTEP).setBlockName("blockIron");
    public static final Block STAIR_DOUBLE = new BlockStep(43, true).setHardness(2.0F).setResistance(10.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("stoneSlab");
    public static final Block STAIR_SINGLE = new BlockStep(44, false).setHardness(2.0F).setResistance(10.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("stoneSlab");
    public static final Block BRICK = new Block(45, 7, Material.ROCK).setHardness(2.0F).setResistance(10.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("brick");
    public static final Block TNT = new BlockTNT(46, 8).setHardness(0.0F).setStepSound(SOUND_GRASS_FOOTSTEP).setBlockName("tnt");
    public static final Block BOOKSHELF = new BlockBookshelf(47, 35).setHardness(1.5F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("bookshelf");
    public static final Block COBBLESTONE_MOSSY = new Block(48, 36, Material.ROCK).setHardness(2.0F).setResistance(10.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("stoneMoss");
    public static final Block OBSIDIAN = new BlockObsidian(49, 37).setHardness(10.0F).setResistance(2000.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("obsidian");
    public static final Block TORCH_WOOD = new BlockTorch(50, 80).setHardness(0.0F).setLightValue(0.9375F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("torch").disableNeighborNotifyOnMetadataChange();
    public static final BlockFire FIRE = (BlockFire) new BlockFire(51, 31).setHardness(0.0F).setLightValue(1.0F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("fire").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block MOB_SPAWNER = new BlockMobSpawner(52, 65).setHardness(5.0F).setStepSound(SOUND_METAL_FOOTSTEP).setBlockName("mobSpawner").disableStats();
    public static final Block STAIR_COMPACT_PLANKS = new BlockStairs(53, PLANKS).setBlockName("stairsWood").disableNeighborNotifyOnMetadataChange();
    public static final Block CHEST = new BlockChest(54).setHardness(2.5F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("chest").disableNeighborNotifyOnMetadataChange();
    public static final Block REDSTONE_WIRE = new BlockRedstoneWire(55, 164).setHardness(0.0F).setStepSound(SOUND_POWDER_FOOTSTEP).setBlockName("redstoneDust").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block ORE_DIAMOND = new BlockOre(56, 50).setHardness(3.0F).setResistance(5.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("oreDiamond");
    public static final Block BLOCK_DIAMOND = new BlockOreStorage(57, 24).setHardness(5.0F).setResistance(10.0F).setStepSound(SOUND_METAL_FOOTSTEP).setBlockName("blockDiamond");
    public static final Block WORKBENCH = new BlockWorkbench(58).setHardness(2.5F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("workbench");
    public static final Block CROPS = new BlockCrops(59, 88).setHardness(0.0F).setStepSound(SOUND_GRASS_FOOTSTEP).setBlockName("crops").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block FARMLAND = new BlockFarmland(60).setHardness(0.6F).setStepSound(SOUND_GRAVEL_FOOTSTEP).setBlockName("farmland");
    public static final Block FURNACE = new BlockFurnace(61, false).setHardness(3.5F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("furnace").disableNeighborNotifyOnMetadataChange();
    public static final Block FURNACE_ACTIVE = new BlockFurnace(62, true).setHardness(3.5F).setStepSound(SOUND_STONE_FOOTSTEP).setLightValue(0.875F).setBlockName("furnace").disableNeighborNotifyOnMetadataChange();
    public static final Block SIGN = new BlockSign(63, TileEntitySign.class, true).setHardness(1.0F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("sign").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block DOOR_WOOD = new BlockDoor(64, Material.WOOD).setHardness(3.0F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("doorWood").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block LADDER = new BlockLadder(65, 83).setHardness(0.4F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("ladder").disableNeighborNotifyOnMetadataChange();
    public static final Block RAIL = new BlockRail(66, 128, false).setHardness(0.7F).setStepSound(SOUND_METAL_FOOTSTEP).setBlockName("rail").disableNeighborNotifyOnMetadataChange();
    public static final Block STAIR_COMPACT_COBBLESTONE = new BlockStairs(67, COBBLESTONE).setBlockName("stairsStone").disableNeighborNotifyOnMetadataChange();
    public static final Block SIGN_WALL = new BlockSign(68, TileEntitySign.class, false).setHardness(1.0F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("sign").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block LEVER = new BlockLever(69, 96).setHardness(0.5F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("lever").disableNeighborNotifyOnMetadataChange();
    public static final Block PRESSURE_PLATE_STONE;
    public static final Block DOOR_STEEL = new BlockDoor(71, Material.IRON).setHardness(5.0F).setStepSound(SOUND_METAL_FOOTSTEP).setBlockName("doorIron").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block PRESSURE_PLATE_PLANKS;
    public static final Block ORE_REDSTONE = new BlockRedstoneOre(73, 51, false).setHardness(3.0F).setResistance(5.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("oreRedstone").disableNeighborNotifyOnMetadataChange();
    public static final Block ORE_REDSTONE_GLOWING = new BlockRedstoneOre(74, 51, true).setLightValue(0.625F).setHardness(3.0F).setResistance(5.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("oreRedstone").disableNeighborNotifyOnMetadataChange();
    public static final Block TORCH_REDSTONE_IDLE = new BlockRedstoneTorch(75, 115, false).setHardness(0.0F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("notGate").disableNeighborNotifyOnMetadataChange();
    public static final Block TORCH_REDSTONE_ACTIVE = new BlockRedstoneTorch(76, 99, true).setHardness(0.0F).setLightValue(0.5F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("notGate").disableNeighborNotifyOnMetadataChange();
    public static final Block BUTTON;
    public static final Block SNOW = new BlockSnow(78, 66).setHardness(0.1F).setStepSound(SOUND_CLOTH_FOOTSTEP).setBlockName("snow");
    public static final Block ICE = new BlockIce(79, 67).setHardness(0.5F).setLightOpacity(3).setStepSound(SOUND_GLASS_FOOTSTEP).setBlockName("ice");
    public static final Block BLOCK_SNOW = new BlockSnowBlock(80, 66).setHardness(0.2F).setStepSound(SOUND_CLOTH_FOOTSTEP).setBlockName("snow");
    public static final Block CACTUS = new BlockCactus(81, 70).setHardness(0.4F).setStepSound(SOUND_CLOTH_FOOTSTEP).setBlockName("cactus");
    public static final Block BLOCK_CLAY = new BlockClay(82, 72).setHardness(0.6F).setStepSound(SOUND_GRAVEL_FOOTSTEP).setBlockName("clay");
    public static final Block REEDS = new BlockReed(83, 73).setHardness(0.0F).setStepSound(SOUND_GRASS_FOOTSTEP).setBlockName("reeds").disableStats();
    public static final Block JUKEBOX = new BlockJukeBox(84, 74).setHardness(2.0F).setResistance(10.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("jukebox").disableNeighborNotifyOnMetadataChange();
    public static final Block FENCE = new BlockFence(85, 4).setHardness(2.0F).setResistance(5.0F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("fence").disableNeighborNotifyOnMetadataChange();
    public static final Block PUMPKIN = new BlockPumpkin(86, 102, false).setHardness(1.0F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("pumpkin").disableNeighborNotifyOnMetadataChange();
    public static final Block BLOOD_STONE = new BlockNetherrack(87, 103).setHardness(0.4F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("hellrock");
    public static final Block SOUL_SAND = new BlockSoulSand(88, 104).setHardness(0.5F).setStepSound(SOUND_SAND_FOOTSTEP).setBlockName("hellsand");
    public static final Block GLOW_STONE = new BlockGlowStone(89, 105, Material.ROCK).setHardness(0.3F).setStepSound(SOUND_GLASS_FOOTSTEP).setLightValue(1.0F).setBlockName("lightgem");
    public static final BlockPortal PORTAL = (BlockPortal) new BlockPortal(90, 14).setHardness(-1.0F).setStepSound(SOUND_GLASS_FOOTSTEP).setLightValue(0.75F).setBlockName("portal");
    public static final Block PUMPKIN_LANTERN = new BlockPumpkin(91, 102, true).setHardness(1.0F).setStepSound(SOUND_WOOD_FOOTSTEP).setLightValue(1.0F).setBlockName("litpumpkin").disableNeighborNotifyOnMetadataChange();
    public static final Block CAKE = new BlockCake(92, 121).setHardness(0.5F).setStepSound(SOUND_CLOTH_FOOTSTEP).setBlockName("cake").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block REDSTONE_REPEATER_IDLE = new BlockRedstoneRepeater(93, false).setHardness(0.0F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("diode").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block REDSTONE_REPEATER_ACTIVE = new BlockRedstoneRepeater(94, true).setHardness(0.0F).setLightValue(0.625F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("diode").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block LOCKED_CHEST = new BlockLockedChest(95).setHardness(0.0F).setLightValue(1.0F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("lockedchest").setTickOnLoad(true).disableNeighborNotifyOnMetadataChange();
    public static final Block TRAPDOOR = new BlockTrapDoor(96, Material.WOOD).setHardness(3.0F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("trapdoor").disableStats().disableNeighborNotifyOnMetadataChange();

    static {
        PRESSURE_PLATE_STONE = new BlockPressurePlate(70, STONE.blockIndexInTexture, EnumMobType.MOBS, Material.ROCK).setHardness(0.5F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("pressurePlate").disableNeighborNotifyOnMetadataChange();
        PRESSURE_PLATE_PLANKS = new BlockPressurePlate(72, PLANKS.blockIndexInTexture, EnumMobType.EVERYTHING, Material.WOOD).setHardness(0.5F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("pressurePlate").disableNeighborNotifyOnMetadataChange();
        BUTTON = new BlockButton(77, STONE.blockIndexInTexture).setHardness(0.5F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("button").disableNeighborNotifyOnMetadataChange();
        Item.ITEMS_LIST[CLOTH.blockID] = new ItemCloth(CLOTH.blockID - 256).setItemName("cloth");
        Item.ITEMS_LIST[WOOD.blockID] = new ItemLog(WOOD.blockID - 256).setItemName("log");
        Item.ITEMS_LIST[STAIR_SINGLE.blockID] = new ItemSlab(STAIR_SINGLE.blockID - 256).setItemName("stoneSlab");
        Item.ITEMS_LIST[SAPLING.blockID] = new ItemSapling(SAPLING.blockID - 256).setItemName("sapling");
        Item.ITEMS_LIST[LEAVES.blockID] = new ItemLeaves(LEAVES.blockID - 256).setItemName("leaves");
        Item.ITEMS_LIST[PISTON_BASE.blockID] = new ItemPiston(PISTON_BASE.blockID - 256);
        Item.ITEMS_LIST[PISTON_STICKY_BASE.blockID] = new ItemPiston(PISTON_STICKY_BASE.blockID - 256);

        for (int i = 0; i < 256; ++i) {
            if (BLOCKS_LIST[i] != null && Item.ITEMS_LIST[i] == null) {
                Item.ITEMS_LIST[i] = new ItemBlock(i - 256);
                BLOCKS_LIST[i].setFireBurnRates();
            }
        }

        CAN_BLOCK_GRASS[0] = true;
        StatList.func_25088_a();
    }

    public final int blockID;
    public final Material blockMaterial;
    public int blockIndexInTexture;
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;
    public StepSound stepSound;
    public float blockParticleGravity;
    public float slipperiness;
    protected float blockHardness;
    protected float blockResistance;
    protected boolean blockConstructorCalled;
    protected boolean enableStats;
    private String blockName;

    protected Block(int id, Material material) {
        this.blockConstructorCalled = true;
        this.enableStats = true;
        this.stepSound = SOUND_POWDER_FOOTSTEP;
        this.blockParticleGravity = 1.0F;
        this.slipperiness = 0.6F;

        if (BLOCKS_LIST[id] != null) {
            throw new IllegalArgumentException("Slot " + id + " is already occupied by " + BLOCKS_LIST[id] + " when adding " + this);
        }

        this.blockMaterial = material;
        BLOCKS_LIST[id] = this;
        this.blockID = id;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        OPAQUE_CUBE_LOOKUP[id] = this.isOpaqueCube();
        LIGHT_OPACITY[id] = this.isOpaqueCube() ? 255 : 0;
        CAN_BLOCK_GRASS[id] = !material.getCanBlockGrass();
        IS_BLOCK_CONTAINER[id] = false;
    }

    protected Block(int id, int blockIndexInTexture, Material material) {
        this(id, material);
        this.blockIndexInTexture = blockIndexInTexture;
    }

    protected Block disableNeighborNotifyOnMetadataChange() {
        REQUIRES_SELF_NOTIFY[this.blockID] = true;
        return this;
    }

    protected void setFireBurnRates() {
    }

    protected Block setStepSound(StepSound stepSound) {
        this.stepSound = stepSound;
        return this;
    }

    protected Block setLightOpacity(int lightOpacity) {
        Block.LIGHT_OPACITY[this.blockID] = lightOpacity;
        return this;
    }

    protected Block setLightValue(float lightValue) {
        Block.LIGHT_VALUE[this.blockID] = (int) (15.0F * lightValue);
        return this;
    }

    protected Block setResistance(float resistance) {
        this.blockResistance = resistance * 3.0F;
        return this;
    }

    public boolean isACube() {
        return true;
    }

    protected Block setBlockUnbreakable() {
        this.setHardness(-1.0F);
        return this;
    }

    public float getHardness() {
        return this.blockHardness;
    }

    protected Block setHardness(float hardness) {
        this.blockHardness = hardness;
        if (this.blockResistance < hardness * 5.0F) {
            this.blockResistance = hardness * 5.0F;
        }

        return this;
    }

    protected Block setTickOnLoad(boolean tickOnLoad) {
        Block.TICK_ON_LOAD[this.blockID] = tickOnLoad;
        return this;
    }

    public void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = (double) minX;
        this.minY = (double) minY;
        this.minZ = (double) minZ;
        this.maxX = (double) maxX;
        this.maxY = (double) maxY;
        this.maxZ = (double) maxZ;
    }

    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int var5) {
        return blockAccess.getBlockMaterial(x, y, z).isSolid();
    }

    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        return this.getBlockTextureFromSide(var1);
    }

    public int getBlockTextureFromSide(int var1) {
        return this.blockIndexInTexture;
    }

    public void getCollidingBoundingBoxes(World world, int x, int y, int z, AxisAlignedBB var5, List<AxisAlignedBB> var6) {
        AxisAlignedBB axis = this.getCollisionBoundingBoxFromPool(world, x, y, z);
        if (axis != null && var5.intersectsWith(axis)) {
            var6.add(axis);
        }

    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBoxFromPool((double) x + this.minX, (double) y + this.minY, (double) z + this.minZ, (double) x + this.maxX, (double) y + this.maxY, (double) z + this.maxZ);
    }

    public boolean isOpaqueCube() {
        return true;
    }

    public boolean canCollideCheck(int var1, boolean var2) {
        return this.isCollidable();
    }

    public boolean isCollidable() {
        return true;
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
    }

    public void onBlockDestroyedByPlayer(World world, int var2, int var3, int var4, int var5) {
    }

    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
    }

    public int tickRate() {
        return 10;
    }

    public void onBlockAdded(World world, int x, int y, int z) {
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
    }

    public int quantityDropped(Random random) {
        return 1;
    }

    public int idDropped(int var1, Random random) {
        return this.blockID;
    }

    public float blockStrength(EntityPlayer entityPlayer) {
        if (this.blockHardness < 0.0F) {
            return 0.0F;
        }

        return !entityPlayer.canHarvestBlock(this)
                ? 1.0F / this.blockHardness / 100.0F
                : entityPlayer.getCurrentPlayerStrVsBlock(this) / this.blockHardness / 30.0F;
    }

    public final void dropBlockAsItem(World world, int x, int y, int z, int var5) {
        this.dropBlockAsItemWithChance(world, x, y, z, var5, 1.0F);
    }

    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int var5, float chance) {
        if (!world.singleplayerWorld) {
            int var7 = this.quantityDropped(world.rand);

            for (int var8 = 0; var8 < var7; ++var8) {
                if (world.rand.nextFloat() <= chance) {
                    int var9 = this.idDropped(var5, world.rand);
                    if (var9 > 0) {
                        this.dropBlockAsItem_do(world, x, y, z, new ItemStack(var9, 1, this.damageDropped(var5)));
                    }
                }
            }

        }
    }

    protected void dropBlockAsItem_do(World world, int var2, int var3, int var4, ItemStack var5) {
        if (!world.singleplayerWorld) {
            float var6 = 0.7F;
            double var7 = (double) (world.rand.nextFloat() * var6) + (double) (1.0F - var6) * 0.5D;
            double var9 = (double) (world.rand.nextFloat() * var6) + (double) (1.0F - var6) * 0.5D;
            double var11 = (double) (world.rand.nextFloat() * var6) + (double) (1.0F - var6) * 0.5D;
            EntityItem var13 = new EntityItem(world, (double) var2 + var7, (double) var3 + var9, (double) var4 + var11, var5);
            var13.delayBeforeCanPickup = 10;
            world.entityJoinedWorld(var13);
        }
    }

    protected int damageDropped(int var1) {
        return 0;
    }

    public float getExplosionResistance(Entity entity) {
        return this.blockResistance / 5.0F;
    }

    public MovingObjectPosition collisionRayTrace(World world, int var2, int var3, int var4, Vec3D var5, Vec3D var6) {
        this.setBlockBoundsBasedOnState(world, var2, var3, var4);
        var5 = var5.addVector((double) (-var2), (double) (-var3), (double) (-var4));
        var6 = var6.addVector((double) (-var2), (double) (-var3), (double) (-var4));
        Vec3D var7 = var5.getIntermediateWithXValue(var6, this.minX);
        Vec3D var8 = var5.getIntermediateWithXValue(var6, this.maxX);
        Vec3D var9 = var5.getIntermediateWithYValue(var6, this.minY);
        Vec3D var10 = var5.getIntermediateWithYValue(var6, this.maxY);
        Vec3D var11 = var5.getIntermediateWithZValue(var6, this.minZ);
        Vec3D var12 = var5.getIntermediateWithZValue(var6, this.maxZ);
        if (!this.isVecInsideYZBounds(var7)) {
            var7 = null;
        }

        if (!this.isVecInsideYZBounds(var8)) {
            var8 = null;
        }

        if (!this.isVecInsideXZBounds(var9)) {
            var9 = null;
        }

        if (!this.isVecInsideXZBounds(var10)) {
            var10 = null;
        }

        if (!this.isVecInsideXYBounds(var11)) {
            var11 = null;
        }

        if (!this.isVecInsideXYBounds(var12)) {
            var12 = null;
        }

        Vec3D var13 = null;
        if (var7 != null && (var13 == null || var5.distanceTo(var7) < var5.distanceTo(var13))) {
            var13 = var7;
        }

        if (var8 != null && (var13 == null || var5.distanceTo(var8) < var5.distanceTo(var13))) {
            var13 = var8;
        }

        if (var9 != null && (var13 == null || var5.distanceTo(var9) < var5.distanceTo(var13))) {
            var13 = var9;
        }

        if (var10 != null && (var13 == null || var5.distanceTo(var10) < var5.distanceTo(var13))) {
            var13 = var10;
        }

        if (var11 != null && (var13 == null || var5.distanceTo(var11) < var5.distanceTo(var13))) {
            var13 = var11;
        }

        if (var12 != null && (var13 == null || var5.distanceTo(var12) < var5.distanceTo(var13))) {
            var13 = var12;
        }

        if (var13 == null) {
            return null;
        } else {
            byte var14 = -1;
            if (var13 == var7) {
                var14 = 4;
            }

            if (var13 == var8) {
                var14 = 5;
            }

            if (var13 == var9) {
                var14 = 0;
            }

            if (var13 == var10) {
                var14 = 1;
            }

            if (var13 == var11) {
                var14 = 2;
            }

            if (var13 == var12) {
                var14 = 3;
            }

            return new MovingObjectPosition(var2, var3, var4, var14, var13.addVector((double) var2, (double) var3, (double) var4));
        }
    }

    private boolean isVecInsideYZBounds(Vec3D vec) {
        if (vec == null) {
            return false;
        } else {
            return vec.yCoord >= this.minY && vec.yCoord <= this.maxY && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
        }
    }

    private boolean isVecInsideXZBounds(Vec3D vec) {
        if (vec == null) {
            return false;
        } else {
            return vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
        }
    }

    private boolean isVecInsideXYBounds(Vec3D vec) {
        if (vec == null) {
            return false;
        } else {
            return vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.yCoord >= this.minY && vec.yCoord <= this.maxY;
        }
    }

    public void onBlockDestroyedByExplosion(World world, int x, int y, int z) {
    }

    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int var5) {
        return this.canPlaceBlockAt(world, x, y, z);
    }

    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        int var5 = world.getBlockId(var2, var3, var4);
        return var5 == 0 || BLOCKS_LIST[var5].blockMaterial.isGroundCover();
    }

    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        return false;
    }

    public void onEntityWalking(World world, int var2, int var3, int var4, Entity entity) {
    }

    public void onBlockPlaced(World world, int var2, int var3, int var4, int var5) {
    }

    public void onBlockClicked(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
    }

    public void velocityToAddToEntity(World world, int var2, int var3, int var4, Entity entity, Vec3D vec) {
    }

    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int var2, int var3, int var4) {
    }

    public boolean isPoweringTo(IBlockAccess blockAccess, int var2, int var3, int var4, int var5) {
        return false;
    }

    public boolean canProvidePower() {
        return false;
    }

    public void onEntityCollidedWithBlock(World world, int var2, int var3, int var4, Entity entity) {
    }

    public boolean isIndirectlyPoweringTo(World world, int var2, int var3, int var4, int var5) {
        return false;
    }

    public void harvestBlock(World world, EntityPlayer entityPlayer, int var3, int var4, int var5, int var6) {
        entityPlayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        this.dropBlockAsItem(world, var3, var4, var5, var6);
    }

    public boolean canBlockStay(World world, int x, int y, int z) {
        return true;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving) {
    }

    public String getNameLocalizedForStats() {
        return StatCollector.translateToLocal(this.getBlockName() + ".name");
    }

    public String getBlockName() {
        return this.blockName;
    }

    public Block setBlockName(String name) {
        this.blockName = "tile." + name;
        return this;
    }

    public void playBlock(World world, int var2, int var3, int var4, int var5, int var6) {
    }

    public boolean getEnableStats() {
        return this.enableStats;
    }

    protected Block disableStats() {
        this.enableStats = false;
        return this;
    }

    public int getMobilityFlag() {
        return this.blockMaterial.getMaterialMobility();
    }
}
