package net.minecraft.block;

import net.minecraft.material.Material;
import net.minecraft.world.World;

import java.util.Random;

public class BlockStep extends Block {
    public static final String[] field_22027_a = new String[]{"stone", "sand", "wood", "cobble"};
    private boolean blockType;

    public BlockStep(int var1, boolean var2) {
        super(var1, 6, Material.ROCK);
        this.blockType = var2;
        if (!var2) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }

        this.setLightOpacity(255);
    }

    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        if (var2 == 0) {
            return var1 <= 1 ? 6 : 5;
        } else if (var2 == 1) {
            if (var1 == 0) {
                return 208;
            } else {
                return var1 == 1 ? 176 : 192;
            }
        } else if (var2 == 2) {
            return 4;
        } else {
            return var2 == 3 ? 16 : 6;
        }
    }

    public int getBlockTextureFromSide(int var1) {
        return this.getBlockTextureFromSideAndMetadata(var1, 0);
    }

    public boolean isOpaqueCube() {
        return this.blockType;
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        if (this != Block.STAIR_SINGLE) {
            super.onBlockAdded(world, x, y, z);
        }

        int var5 = world.getBlockId(x, y - 1, z);
        int var6 = world.getBlockMetadata(x, y, z);
        int var7 = world.getBlockMetadata(x, y - 1, z);
        if (var6 == var7) {
            if (var5 == STAIR_SINGLE.blockID) {
                world.setBlockWithNotify(x, y, z, 0);
                world.setBlockAndMetadataWithNotify(x, y - 1, z, Block.STAIR_DOUBLE.blockID, var6);
            }

        }
    }

    public int idDropped(int var1, Random random) {
        return Block.STAIR_SINGLE.blockID;
    }

    public int quantityDropped(Random random) {
        return this.blockType ? 2 : 1;
    }

    protected int damageDropped(int var1) {
        return var1;
    }

    public boolean isACube() {
        return this.blockType;
    }
}
