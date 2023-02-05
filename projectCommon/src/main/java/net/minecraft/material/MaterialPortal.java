package net.minecraft.material;

public class MaterialPortal extends Material {
    public MaterialPortal(MapColor color) {
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
