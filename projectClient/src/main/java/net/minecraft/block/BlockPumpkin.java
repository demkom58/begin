package net.minecraft.block;

import net.minecraft.entity.EntityLiving;
import net.minecraft.material.Material;
import net.minecraft.world.World;
import net.minecraft.util.MathHelper;

public class BlockPumpkin extends Block {
    private boolean blockType;

    protected BlockPumpkin(int var1, int var2, boolean var3) {
        super(var1, Material.PUMPKIN);
        this.blockIndexInTexture = var2;
        this.setTickOnLoad(true);
        this.blockType = var3;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        if (side == 1) {
            return this.blockIndexInTexture;
        } else if (side == 0) {
            return this.blockIndexInTexture;
        } else {
            int var3 = this.blockIndexInTexture + 1 + 16;
            if (this.blockType) {
                ++var3;
            }

            if (metadata == 2 && side == 2) {
                return var3;
            } else if (metadata == 3 && side == 5) {
                return var3;
            } else if (metadata == 0 && side == 3) {
                return var3;
            } else {
                return metadata == 1 && side == 4 ? var3 : this.blockIndexInTexture + 16;
            }
        }
    }

    @Override
    public int getBlockTextureFromSide(int side) {
        if (side == 1) {
            return this.blockIndexInTexture;
        } else if (side == 0) {
            return this.blockIndexInTexture;
        } else {
            return side == 3 ? this.blockIndexInTexture + 1 + 16 : this.blockIndexInTexture + 16;
        }
    }

    @Override
    public void onBlockAdded(World var1, int var2, int var3, int var4) {
        super.onBlockAdded(var1, var2, var3, var4);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        int var5 = world.getBlockId(x, y, z);
        return (var5 == 0 || Block.BLOCKS_LIST[var5].blockMaterial.getIsGroundCover()) && world.isBlockNormalCube(x, y - 1, z);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity) {
        int var6 = MathHelper.floor((double) (entity.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, var6);
    }
}
