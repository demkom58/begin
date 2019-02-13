package net.minecraft;

public class Material {
    public static final Material air = new MaterialTransparent(MapColor.field_28199_b);
    public static final Material grass = new Material(MapColor.field_28198_c);
    public static final Material ground = new Material(MapColor.field_28189_l);
    public static final Material wood = (new Material(MapColor.field_28186_o)).setBurning();
    public static final Material rock = (new Material(MapColor.field_28188_m)).func_31058_n();
    public static final Material iron = (new Material(MapColor.field_28193_h)).func_31058_n();
    public static final Material water = (new MaterialLiquid(MapColor.field_28187_n)).setNoPushMobility();
    public static final Material lava = (new MaterialLiquid(MapColor.field_28195_f)).setNoPushMobility();
    public static final Material leaves = (new Material(MapColor.field_28192_i)).setBurning().func_28129_i().setNoPushMobility();
    public static final Material plants = (new MaterialLogic(MapColor.field_28192_i)).setNoPushMobility();
    public static final Material sponge = new Material(MapColor.field_28196_e);
    public static final Material cloth = (new Material(MapColor.field_28196_e)).setBurning();
    public static final Material fire = (new MaterialTransparent(MapColor.field_28199_b)).setNoPushMobility();
    public static final Material sand = new Material(MapColor.field_28197_d);
    public static final Material circuits = (new MaterialLogic(MapColor.field_28199_b)).setNoPushMobility();
    public static final Material glass = (new Material(MapColor.field_28199_b)).func_28129_i();
    public static final Material tnt = (new Material(MapColor.field_28195_f)).setBurning().func_28129_i();
    public static final Material wug = (new Material(MapColor.field_28192_i)).setNoPushMobility();
    public static final Material ice = (new Material(MapColor.field_28194_g)).func_28129_i();
    public static final Material snow = (new MaterialLogic(MapColor.field_28191_j)).func_27089_f().func_28129_i().func_31058_n().setNoPushMobility();
    public static final Material builtSnow = (new Material(MapColor.field_28191_j)).func_31058_n();
    public static final Material cactus = (new Material(MapColor.field_28192_i)).func_28129_i().setNoPushMobility();
    public static final Material clay = new Material(MapColor.field_28190_k);
    public static final Material pumpkin = (new Material(MapColor.field_28192_i)).setNoPushMobility();
    public static final Material portal = (new MaterialPortal(MapColor.field_28199_b)).setImmovableMobility();
    public static final Material cakeMaterial = (new Material(MapColor.field_28199_b)).setNoPushMobility();
    public static final Material web = (new Material(MapColor.field_28196_e)).func_31058_n().setNoPushMobility();
    public static final Material piston = (new Material(MapColor.field_28188_m)).setImmovableMobility();
    public final MapColor field_28131_A;
    private boolean canBurn;
    private boolean field_27091_A;
    private boolean isTranslucent;
    private boolean field_31061_G = true;
    private int mobilityFlag;

    public Material(MapColor var1) {
        this.field_28131_A = var1;
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

    private Material func_28129_i() {
        this.isTranslucent = true;
        return this;
    }

    private Material func_31058_n() {
        this.field_31061_G = false;
        return this;
    }

    private Material setBurning() {
        this.canBurn = true;
        return this;
    }

    public boolean getBurning() {
        return this.canBurn;
    }

    public Material func_27089_f() {
        this.field_27091_A = true;
        return this;
    }

    public boolean func_27090_g() {
        return this.field_27091_A;
    }

    public boolean getIsOpaque() {
        return !this.isTranslucent && this.getIsSolid();
    }

    public boolean func_31055_i() {
        return this.field_31061_G;
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
