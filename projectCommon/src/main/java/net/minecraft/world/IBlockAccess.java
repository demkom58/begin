package net.minecraft.world;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.material.Material;
import net.minecraft.tileentity.TileEntity;

public interface IBlockAccess {
    int getBlockId(int x, int y, int z);

    TileEntity getBlockTileEntity(int x, int y, int z);

    @Side(CodeSide.CLIENT)
    float getBrightness(int x, int y, int z, int minValue);

    @Side(CodeSide.CLIENT)
    float getLightBrightness(int x, int y, int z);

    int getBlockMetadata(int x, int y, int z);

    Material getBlockMaterial(int x, int y, int z);

    @Side(CodeSide.CLIENT)
    boolean isBlockOpaqueCube(int x, int y, int z);

    boolean isBlockNormalCube(int x, int y, int z);

    WorldChunkManager getWorldChunkManager();
}
