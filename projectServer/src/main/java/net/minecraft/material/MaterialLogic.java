package net.minecraft.material;

public class MaterialLogic extends Material {
    public MaterialLogic(MapColor var1) {
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
