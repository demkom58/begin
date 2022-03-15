package net.potion.world;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.material.Material;
import net.potion.tileentity.TileEntity;

public interface IBlockAccess {
    int getBlockId(int var1, int var2, int var3);

    TileEntity getBlockTileEntity(int var1, int var2, int var3);

    @Side(CodeSide.CLIENT)
    float getBrightness(int var1, int var2, int var3, int var4);

    @Side(CodeSide.CLIENT)
    float getLightBrightness(int var1, int var2, int var3);

    int getBlockMetadata(int var1, int var2, int var3);

    Material getBlockMaterial(int var1, int var2, int var3);

    @Side(CodeSide.CLIENT)
    boolean isBlockOpaqueCube(int var1, int var2, int var3);

    boolean isBlockNormalCube(int var1, int var2, int var3);

    WorldChunkManager getWorldChunkManager();
}
