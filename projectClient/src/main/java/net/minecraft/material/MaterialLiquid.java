package net.minecraft.material;

public class MaterialLiquid extends Material {
    public MaterialLiquid(MapColor var1) {
        super(var1);
        this.setIsGroundCover();
        this.setNoPushMobility();
    }

    @Override
    public boolean getIsLiquid() {
        return true;
    }

    @Override
    public boolean getIsSolid() {
        return false;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
