package net.minecraft.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockLeavesBase extends Block {
    protected boolean graphicsLevel;

    protected BlockLeavesBase(int var1, int var2, Material var3, boolean var4) {
        super(var1, var2, var3);
        this.graphicsLevel = var4;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        int var6 = blockAccess.getBlockId(x, y, z);
        return (this.graphicsLevel || var6 != this.blockID) && super.shouldSideBeRendered(blockAccess, x, y, z, side);
    }
}
