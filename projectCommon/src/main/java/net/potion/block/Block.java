package net.potion.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.hypnosis.util.math.MathHelper;
import net.hypnosis.util.math.Vec3d;
import net.potion.entity.Entity;
import net.potion.entity.EntityLiving;
import net.potion.entity.EnumMobType;
import net.potion.entity.item.EntityItem;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.*;
import net.potion.material.Material;
import net.potion.stats.StatCollector;
import net.potion.stats.StatList;
import net.potion.tileentity.TileEntitySign;
import net.potion.util.AxisAlignedBB;
import net.potion.util.MovingObjectPosition;
import net.potion.world.IBlockAccess;
import net.potion.world.World;

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
    public static final Block PRESSURE_PLATE_STONE = new BlockPressurePlate(70, STONE.blockIndexInTexture, EnumMobType.MOBS, Material.ROCK).setHardness(0.5F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("pressurePlate").disableNeighborNotifyOnMetadataChange();
    public static final Block DOOR_IRON = new BlockDoor(71, Material.IRON).setHardness(5.0F).setStepSound(SOUND_METAL_FOOTSTEP).setBlockName("doorIron").disableStats().disableNeighborNotifyOnMetadataChange();
    public static final Block PRESSURE_PLATE_PLANKS = (new BlockPressurePlate(72, PLANKS.blockIndexInTexture, EnumMobType.EVERYTHING, Material.WOOD)).setHardness(0.5F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("pressurePlate").disableNeighborNotifyOnMetadataChange();
    public static final Block ORE_REDSTONE = new BlockRedstoneOre(73, 51, false).setHardness(3.0F).setResistance(5.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("oreRedstone").disableNeighborNotifyOnMetadataChange();
    public static final Block ORE_REDSTONE_GLOWING = new BlockRedstoneOre(74, 51, true).setLightValue(0.625F).setHardness(3.0F).setResistance(5.0F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("oreRedstone").disableNeighborNotifyOnMetadataChange();
    public static final Block TORCH_REDSTONE_IDLE = new BlockRedstoneTorch(75, 115, false).setHardness(0.0F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("notGate").disableNeighborNotifyOnMetadataChange();
    public static final Block TORCH_REDSTONE_ACTIVE = new BlockRedstoneTorch(76, 99, true).setHardness(0.0F).setLightValue(0.5F).setStepSound(SOUND_WOOD_FOOTSTEP).setBlockName("notGate").disableNeighborNotifyOnMetadataChange();
    public static final Block BUTTON = new BlockButton(77, STONE.blockIndexInTexture).setHardness(0.5F).setStepSound(SOUND_STONE_FOOTSTEP).setBlockName("button").disableNeighborNotifyOnMetadataChange();
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
        Item.ITEMS_LIST[CLOTH.blockID] = (new ItemCloth(CLOTH.blockID - 256)).setItemName("cloth");
        Item.ITEMS_LIST[WOOD.blockID] = (new ItemLog(WOOD.blockID - 256)).setItemName("log");
        Item.ITEMS_LIST[STAIR_SINGLE.blockID] = (new ItemSlab(STAIR_SINGLE.blockID - 256)).setItemName("stoneSlab");
        Item.ITEMS_LIST[SAPLING.blockID] = (new ItemSapling(SAPLING.blockID - 256)).setItemName("sapling");
        Item.ITEMS_LIST[LEAVES.blockID] = (new ItemLeaves(LEAVES.blockID - 256)).setItemName("leaves");
        Item.ITEMS_LIST[PISTON_BASE.blockID] = new ItemPiston(PISTON_BASE.blockID - 256);
        Item.ITEMS_LIST[PISTON_STICKY_BASE.blockID] = new ItemPiston(PISTON_STICKY_BASE.blockID - 256);

        for (int id = 0; id < 256; ++id) {
            if (BLOCKS_LIST[id] != null && Item.ITEMS_LIST[id] == null) {
                Item.ITEMS_LIST[id] = new ItemBlock(id - 256);
                BLOCKS_LIST[id].initializeBlock();
            }
        }

        CAN_BLOCK_GRASS[0] = true;
        StatList.method1();
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
        CAN_BLOCK_GRASS[id] = !material.canBlockGrass();
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

    protected void initializeBlock() {
    }

    protected Block setStepSound(StepSound stepSound) {
        this.stepSound = stepSound;
        return this;
    }

    protected Block setLightOpacity(int lightOpacity) {
        LIGHT_OPACITY[this.blockID] = lightOpacity;
        return this;
    }

    protected Block setLightValue(float lightValue) {
        LIGHT_VALUE[this.blockID] = (int) (15.0F * lightValue);
        return this;
    }

    protected Block setResistance(float resistance) {
        this.blockResistance = resistance * 3.0F;
        return this;
    }

    public boolean isNormalCube() {
        return true;
    }

    @Side(CodeSide.CLIENT)
    public int getRenderType() {
        return 0;
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

    protected Block setTickOnLoad(boolean tick) {
        TICK_ON_LOAD[this.blockID] = tick;
        return this;
    }

    public void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public float getBlockBrightness(IBlockAccess blockAccess, int x, int y, int z) {
        return blockAccess.getBrightness(x, y, z, LIGHT_VALUE[this.blockID]);
    }

    @Side(CodeSide.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        if (side == 0 && this.minY > 0.0D) {
            return true;
        } else if (side == 1 && this.maxY < 1.0D) {
            return true;
        } else if (side == 2 && this.minZ > 0.0D) {
            return true;
        } else if (side == 3 && this.maxZ < 1.0D) {
            return true;
        } else if (side == 4 && this.minX > 0.0D) {
            return true;
        } else if (side == 5 && this.maxX < 1.0D) {
            return true;
        } else {
            return !blockAccess.isBlockOpaqueCube(x, y, z);
        }
    }

    public boolean getIsBlockSolid(IBlockAccess blockAccess, int x, int y, int z, int var5) {
        return blockAccess.getBlockMaterial(x, y, z).isSolid();
    }

    @Side(CodeSide.CLIENT)
    public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return this.getBlockTextureFromSideAndMetadata(side, blockAccess.getBlockMetadata(x, y, z));
    }

    @Side(CodeSide.CLIENT)
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        return this.getBlockTextureFromSide(side);
    }

    @Side(CodeSide.CLIENT)
    public int getBlockTextureFromSide(int side) {
        return this.blockIndexInTexture;
    }

    @Side(CodeSide.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBoxFromPool((double) x + this.minX, (double) y + this.minY, (double) z + this.minZ, (double) x + this.maxX, (double) y + this.maxY, (double) z + this.maxZ);
    }

    public void getCollidingBoundingBoxes(World world, int x, int y, int z, AxisAlignedBB bb, List<AxisAlignedBB> bbs) {
        AxisAlignedBB poolBB = this.getCollisionBoundingBoxFromPool(world, x, y, z);
        if (poolBB != null && bb.intersectsWith(poolBB))
            bbs.add(poolBB);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        return AxisAlignedBB.getBoundingBoxFromPool((double) var2 + this.minX, (double) var3 + this.minY, (double) var4 + this.minZ, (double) var2 + this.maxX, (double) var3 + this.maxY, (double) var4 + this.maxZ);
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

    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
    }

    @Side(CodeSide.CLIENT)
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
    }

    public void onBlockDestroyedByPlayer(World var1, int var2, int var3, int var4, int var5) {
    }

    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
    }

    public int tickRate() {
        return 10;
    }

    public void onBlockAdded(World var1, int var2, int var3, int var4) {
    }

    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
    }

    public int quantityDropped(Random var1) {
        return 1;
    }

    public int idDropped(int var1, Random var2) {
        return this.blockID;
    }

    public float blockStrength(EntityPlayer var1) {
        if (this.blockHardness < 0.0F) {
            return 0.0F;
        }

        return !var1.canHarvestBlock(this)
                ? 1.0F / this.blockHardness / 100.0F
                : var1.getCurrentPlayerStrVsBlock(this) / this.blockHardness / 30.0F;
    }

    public final void dropBlockAsItem(World var1, int var2, int var3, int var4, int var5) {
        this.dropBlockAsItemWithChance(var1, var2, var3, var4, var5, 1.0F);
    }

    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int var5, float failChance) {
        if (world.localWorld) {
            return;
        }

        int quantityDropped = this.quantityDropped(world.rand);
        for (int i = 0; i < quantityDropped; ++i) {
            if (world.rand.nextFloat() <= failChance) {
                int var9 = this.idDropped(var5, world.rand);
                if (var9 > 0) {
                    this.dropBlockAsItem_do(world, x, y, z, new ItemStack(var9, 1, this.damageDropped(var5)));
                }
            }
        }
    }

    protected void dropBlockAsItem_do(World world, int x, int y, int z, ItemStack stack) {
        if (!world.localWorld) {
            float var6 = 0.7F;
            double var7 = (double) (world.rand.nextFloat() * var6) + (double) (1.0F - var6) * 0.5D;
            double var9 = (double) (world.rand.nextFloat() * var6) + (double) (1.0F - var6) * 0.5D;
            double var11 = (double) (world.rand.nextFloat() * var6) + (double) (1.0F - var6) * 0.5D;
            EntityItem var13 = new EntityItem(world, (double) x + var7, (double) y + var9, (double) z + var11, stack);
            var13.delayBeforeCanPickup = 10;
            world.entityJoinedWorld(var13);
        }
    }

    protected int damageDropped(int var1) {
        return 0;
    }

    public float getExplosionResistance(Entity var1) {
        return this.blockResistance / 5.0F;
    }

    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3d var5, Vec3d var6) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        var5 = new Vec3d(var5).add(-x, -y, -z);
        var6 = new Vec3d(var6).add(-x, -y, -z);
        Vec3d var7 = MathHelper.getIntermediateWithXValue(var5, var6, this.minX);
        Vec3d var8 = MathHelper.getIntermediateWithXValue(var5, var6, this.maxX);
        Vec3d var9 = MathHelper.getIntermediateWithYValue(var5, var6, this.minY);
        Vec3d var10 = MathHelper.getIntermediateWithYValue(var5, var6, this.maxY);
        Vec3d var11 = MathHelper.getIntermediateWithZValue(var5, var6, this.minZ);
        Vec3d var12 = MathHelper.getIntermediateWithZValue(var5, var6, this.maxZ);
        if (!this.isVecInsideYZBounds(var7))
            var7 = null;

        if (!this.isVecInsideYZBounds(var8))
            var8 = null;

        if (!this.isVecInsideXZBounds(var9))
            var9 = null;

        if (!this.isVecInsideXZBounds(var10))
            var10 = null;

        if (!this.isVecInsideXYBounds(var11))
            var11 = null;

        if (!this.isVecInsideXYBounds(var12))
            var12 = null;

        Vec3d var13 = null;
        if (var7 != null && (var13 == null || var5.distanceTo(var7) < var5.distanceTo(var13)))
            var13 = var7;

        if (var8 != null && (var13 == null || var5.distanceTo(var8) < var5.distanceTo(var13)))
            var13 = var8;

        if (var9 != null && (var13 == null || var5.distanceTo(var9) < var5.distanceTo(var13)))
            var13 = var9;

        if (var10 != null && (var13 == null || var5.distanceTo(var10) < var5.distanceTo(var13)))
            var13 = var10;

        if (var11 != null && (var13 == null || var5.distanceTo(var11) < var5.distanceTo(var13)))
            var13 = var11;

        if (var12 != null && (var13 == null || var5.distanceTo(var12) < var5.distanceTo(var13)))
            var13 = var12;

        if (var13 == null)
            return null;

        byte var14 = -1;
        if (var13 == var7)
            var14 = 4;

        if (var13 == var8)
            var14 = 5;

        if (var13 == var9)
            var14 = 0;

        if (var13 == var10)
            var14 = 1;

        if (var13 == var11)
            var14 = 2;

        if (var13 == var12)
            var14 = 3;

        return new MovingObjectPosition(x, y, z, var14, new Vec3d(var13).add(x, y, z));
    }

    private boolean isVecInsideYZBounds(Vec3d vec) {
        if (vec == null)
            return false;

        return vec.y >= this.minY && vec.y <= this.maxY && vec.z >= this.minZ && vec.z <= this.maxZ;
    }

    private boolean isVecInsideXZBounds(Vec3d vec) {
        if (vec == null)
            return false;

        return vec.x >= this.minX && vec.x <= this.maxX && vec.z >= this.minZ && vec.z <= this.maxZ;
    }

    private boolean isVecInsideXYBounds(Vec3d vec) {
        if (vec == null)
            return false;

        return vec.x >= this.minX && vec.x <= this.maxX && vec.y >= this.minY && vec.y <= this.maxY;
    }

    public void onBlockDestroyedByExplosion(World world, int x, int y, int z) {
    }

    @Side(CodeSide.CLIENT)
    public int getRenderBlockPass() {
        return 0;
    }

    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int var5) {
        return this.canPlaceBlockAt(world, x, y, z);
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        int blockId = world.getBlockId(x, y, z);
        return blockId == 0 || BLOCKS_LIST[blockId].blockMaterial.isGroundCover();
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        return false;
    }

    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
    }

    public void onBlockPlaced(World world, int x, int y, int z, int side) {
    }

    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
    }

    public Vec3d velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3d velocity) {
        return Vec3d.ZERO;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
    }

    @Side(CodeSide.CLIENT)
    public int getRenderColor(int var1) {
        return 0xffffff;
    }

    @Side(CodeSide.CLIENT)
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
        return 0xffffff;
    }

    public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int var5) {
        return false;
    }

    public boolean canProvidePower() {
        return false;
    }

    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
    }

    public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int var5) {
        return false;
    }

    @Side(CodeSide.CLIENT)
    public void setBlockBoundsForItemRender() {
    }

    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int blockId) {
        player.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        this.dropBlockAsItem(world, x, y, z, blockId);
    }

    public boolean canBlockStay(World world, int x, int y, int z) {
        return true;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity) {
    }

    public String translateBlockName() {
        return StatCollector.translateToLocal(this.getBlockName() + ".name");
    }

    public String getBlockName() {
        return this.blockName;
    }

    public Block setBlockName(String var1) {
        this.blockName = "tile." + var1;
        return this;
    }

    public void playBlock(World var1, int var2, int var3, int var4, int var5, int var6) {
    }

    public boolean isEnabledStats() {
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
