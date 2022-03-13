package net.potion.material;

public class MaterialLiquid extends Material {
    public MaterialLiquid(MapColor color) {
        super(color);
        this.setGroundCover();
        this.setNoPushMobility();
    }

    @Override
    public boolean isLiquid() {
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
