package net.minecraft;

public abstract class BlockContainer extends Block {
    protected BlockContainer(int var1, Material var2) {
        super(var1, var2);
        IS_BLOCK_CONTAINER[var1] = true;
    }

    protected BlockContainer(int var1, int var2, Material var3) {
        super(var1, var2, var3);
        IS_BLOCK_CONTAINER[var1] = true;
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        world.setBlockTileEntity(x, y, z, this.getBlockEntity());
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        super.onBlockRemoval(world, x, y, z);
        world.removeBlockTileEntity(x, y, z);
    }

    protected abstract TileEntity getBlockEntity();
}
