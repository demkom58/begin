package net.potion.material;

public class MaterialTransparent extends Material {
    public MaterialTransparent(MapColor var1) {
        super(var1);
        this.setGroundCover();
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean getCanBlockGrass() {
        return false;
    }

    @Override
    public boolean getIsSolid() {
        return false;
    }
}
