package net.potion.material;

public class MaterialLogic extends Material {
    public MaterialLogic(MapColor color) {
        super(color);
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
