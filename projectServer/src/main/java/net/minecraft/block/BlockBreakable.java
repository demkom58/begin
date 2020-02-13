package net.minecraft.block;

import net.minecraft.material.Material;

public class BlockBreakable extends Block {
    private boolean field_6084_a;

    protected BlockBreakable(int var1, int var2, Material var3, boolean var4) {
        super(var1, var2, var3);
        this.field_6084_a = var4;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
