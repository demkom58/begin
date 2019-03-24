package net.minecraft.block;

import net.minecraft.material.Material;

public class BlockLeavesBase extends Block {
    protected boolean graphicsLevel;

    protected BlockLeavesBase(int var1, int var2, Material var3, boolean var4) {
        super(var1, var2, var3);
        this.graphicsLevel = var4;
    }

    public boolean isOpaqueCube() {
        return false;
    }
}
