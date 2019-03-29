package net.minecraft.material;

public class Material {
    public static final Material AIR = new MaterialTransparent(MapColor.AIR_COLOR);
    public static final Material GRASS_MATERIAL = new Material(MapColor.GRASS_COLOR);
    public static final Material GROUND = new Material(MapColor.DIRT_COLOR);
    public static final Material WOOD = new Material(MapColor.WOOD_COLOR).setBurning();
    public static final Material ROCK = new Material(MapColor.STONE_COLOR).setNoHarvest();
    public static final Material IRON = new Material(MapColor.IRON_COLOR).setNoHarvest();
    public static final Material WATER = new MaterialLiquid(MapColor.WATER_COLOR).setNoPushMobility();
    public static final Material LAVA = new MaterialLiquid(MapColor.TNT_COLOR).setNoPushMobility();
    public static final Material LEAVES = new Material(MapColor.FOLIAGE_COLOR).setBurning().setIsTranslucent().setNoPushMobility();
    public static final Material PLANTS = new MaterialLogic(MapColor.FOLIAGE_COLOR).setNoPushMobility();
    public static final Material SPONGE = new Material(MapColor.CLOTH_COLOR);
    public static final Material CLOTH = new Material(MapColor.CLOTH_COLOR).setBurning();
    public static final Material FIRE = new MaterialTransparent(MapColor.AIR_COLOR).setNoPushMobility();
    public static final Material SAND = new Material(MapColor.SAND_COLOR);
    public static final Material CIRCUITS = new MaterialLogic(MapColor.AIR_COLOR).setNoPushMobility();
    public static final Material GLASS = new Material(MapColor.AIR_COLOR).setIsTranslucent();
    public static final Material TNT = new Material(MapColor.TNT_COLOR).setBurning().setIsTranslucent();
    public static final Material WUG = new Material(MapColor.FOLIAGE_COLOR).setNoPushMobility();
    public static final Material ICE = new Material(MapColor.ICE_COLOR).setIsTranslucent();
    public static final Material SNOW = new MaterialLogic(MapColor.SNOW_COLOR).setIsGroundCover().setIsTranslucent().setNoHarvest().setNoPushMobility();
    public static final Material BUILT_SNOW = new Material(MapColor.SNOW_COLOR).setNoHarvest();
    public static final Material CACTUS = new Material(MapColor.FOLIAGE_COLOR).setIsTranslucent().setNoPushMobility();
    public static final Material CLAY = new Material(MapColor.CLAY_COLOR);
    public static final Material PUMPKIN = new Material(MapColor.FOLIAGE_COLOR).setNoPushMobility();
    public static final Material PORTAL = new MaterialPortal(MapColor.AIR_COLOR).setImmovableMobility();
    public static final Material CAKE = new Material(MapColor.AIR_COLOR).setNoPushMobility();
    public static final Material WEB = new Material(MapColor.CLOTH_COLOR).setNoHarvest().setNoPushMobility();
    public static final Material PISTON = new Material(MapColor.STONE_COLOR).setImmovableMobility();
    public final MapColor materialMapColor;
    private boolean canBurn;
    private boolean groundCover;
    private boolean isOpaque;
    private boolean canHarvest = true;
    private int mobilityFlag;

    public Material(MapColor materialMapColor) {
        this.materialMapColor = materialMapColor;
    }

    public boolean getIsLiquid() {
        return false;
    }

    public boolean isSolid() {
        return true;
    }

    public boolean getCanBlockGrass() {
        return true;
    }

    public boolean getIsSolid() {
        return true;
    }

    private Material setIsTranslucent() {
        this.isOpaque = true;
        return this;
    }

    private Material setNoHarvest() {
        this.canHarvest = false;
        return this;
    }

    private Material setBurning() {
        this.canBurn = true;
        return this;
    }

    public boolean getBurning() {
        return this.canBurn;
    }

    public Material setIsGroundCover() {
        this.groundCover = true;
        return this;
    }

    public boolean getIsGroundCover() {
        return this.groundCover;
    }

    public boolean getIsTranslucent() {
        return this.isOpaque ? false : this.getIsSolid();
    }

    public boolean getIsHarvestable() {
        return this.canHarvest;
    }

    public int getMaterialMobility() {
        return this.mobilityFlag;
    }

    protected Material setNoPushMobility() {
        this.mobilityFlag = 1;
        return this;
    }

    protected Material setImmovableMobility() {
        this.mobilityFlag = 2;
        return this;
    }
}
