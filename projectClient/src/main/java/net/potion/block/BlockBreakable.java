package net.potion.block;

import net.potion.material.Material;
import net.potion.world.IBlockAccess;

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
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        int var6 = blockAccess.getBlockId(x, y, z);
        return (this.localFlag || var6 != this.blockID) && super.shouldSideBeRendered(blockAccess, x, y, z, side);
    }
}
