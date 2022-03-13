package net.potion.material;

public class MaterialTransparent extends Material {
    public MaterialTransparent(MapColor color) {
        super(color);
        this.setGroundCover();
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean canBlockGrass() {
        return false;
    }

    @Override
    public boolean getIsSolid() {
        return false;
    }
}
