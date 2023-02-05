package net.minecraft.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockBreakable extends Block {
    private final boolean localFlag;

    protected BlockBreakable(int var1, int var2, Material var3, boolean var4) {
        super(var1, var2, var3);
        this.localFlag = var4;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        int var6 = blockAccess.getBlockId(x, y, z);
        return (this.localFlag || var6 != this.blockID) && super.shouldSideBeRendered(blockAccess, x, y, z, side);
    }
}
