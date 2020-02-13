package net.minecraft.block;

import net.minecraft.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockStep extends Block {
    public static final String[] field_22037_a = new String[]{"stone", "sand", "wood", "cobble"};
    private boolean blockType;

    public BlockStep(int var1, boolean var2) {
        super(var1, 6, Material.ROCK);
        this.blockType = var2;
        if (!var2) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }

        this.setLightOpacity(255);
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        if (metadata == 0) {
            return side <= 1 ? 6 : 5;
        } else if (metadata == 1) {
            if (side == 0) {
                return 208;
            } else {
                return side == 1 ? 176 : 192;
            }
        } else if (metadata == 2) {
            return 4;
        } else {
            return metadata == 3 ? 16 : 6;
        }
    }

    @Override
    public int getBlockTextureFromSide(int side) {
        return this.getBlockTextureFromSideAndMetadata(side, 0);
    }

    @Override
    public boolean isOpaqueCube() {
        return this.blockType;
    }

    @Override
    public void onBlockAdded(World var1, int var2, int var3, int var4) {
        if (this != Block.STAIR_SINGLE) {
            super.onBlockAdded(var1, var2, var3, var4);
        }

        int var5 = var1.getBlockId(var2, var3 - 1, var4);
        int var6 = var1.getBlockMetadata(var2, var3, var4);
        int var7 = var1.getBlockMetadata(var2, var3 - 1, var4);
        if (var6 == var7) {
            if (var5 == STAIR_SINGLE.blockID) {
                var1.setBlockWithNotify(var2, var3, var4, 0);
                var1.setBlockAndMetadataWithNotify(var2, var3 - 1, var4, Block.STAIR_DOUBLE.blockID, var6);
            }

        }
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return Block.STAIR_SINGLE.blockID;
    }

    @Override
    public int quantityDropped(Random var1) {
        return this.blockType ? 2 : 1;
    }

    @Override
    protected int damageDropped(int var1) {
        return var1;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return this.blockType;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        if (this != Block.STAIR_SINGLE) {
            super.shouldSideBeRendered(blockAccess, x, y, z, side);
        }

        if (side == 1) {
            return true;
        } else if (!super.shouldSideBeRendered(blockAccess, x, y, z, side)) {
            return false;
        } else if (side == 0) {
            return true;
        } else {
            return blockAccess.getBlockId(x, y, z) != this.blockID;
        }
    }
}
