package net.minecraft.material;

public class MaterialPortal extends Material {
    public MaterialPortal(MapColor var1) {
        super(var1);
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
