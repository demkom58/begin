package net.potion.block;

import net.potion.material.Material;
import net.potion.tileentity.TileEntity;
import net.potion.world.World;

public abstract class BlockContainer extends Block {
    protected BlockContainer(int var1, Material var2) {
        super(var1, var2);
        IS_BLOCK_CONTAINER[var1] = true;
    }

    protected BlockContainer(int var1, int var2, Material var3) {
        super(var1, var2, var3);
        IS_BLOCK_CONTAINER[var1] = true;
    }

    @Override
    public void onBlockAdded(World var1, int var2, int var3, int var4) {
        super.onBlockAdded(var1, var2, var3, var4);
        var1.setBlockTileEntity(var2, var3, var4, this.getBlockEntity());
    }

    @Override
    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        super.onBlockRemoval(var1, var2, var3, var4);
        var1.removeBlockTileEntity(var2, var3, var4);
    }

    protected abstract TileEntity getBlockEntity();
}
