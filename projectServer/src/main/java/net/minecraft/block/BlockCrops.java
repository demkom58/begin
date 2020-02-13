package net.minecraft.block;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCrops extends BlockFlower {
    protected BlockCrops(int var1, int var2) {
        super(var1, var2);
        this.blockIndexInTexture = var2;
        this.setTickOnLoad(true);
        float var3 = 0.5F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.25F, 0.5F + var3);
    }

    @Override
    protected boolean canThisPlantGrowOnThisBlockID(int var1) {
        return var1 == Block.FARMLAND.blockID;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);
        if (world.getBlockLightValue(x, y + 1, z) >= 9) {
            int var6 = world.getBlockMetadata(x, y, z);
            if (var6 < 7) {
                float var7 = this.getGrowthRate(world, x, y, z);
                if (random.nextInt((int) (100.0F / var7)) == 0) {
                    ++var6;
                    world.setBlockMetadataWithNotify(x, y, z, var6);
                }
            }
        }

    }

    public void fertilize(World var1, int var2, int var3, int var4) {
        var1.setBlockMetadataWithNotify(var2, var3, var4, 7);
    }

    private float getGrowthRate(World var1, int var2, int var3, int var4) {
        float var5 = 1.0F;
        int var6 = var1.getBlockId(var2, var3, var4 - 1);
        int var7 = var1.getBlockId(var2, var3, var4 + 1);
        int var8 = var1.getBlockId(var2 - 1, var3, var4);
        int var9 = var1.getBlockId(var2 + 1, var3, var4);
        int var10 = var1.getBlockId(var2 - 1, var3, var4 - 1);
        int var11 = var1.getBlockId(var2 + 1, var3, var4 - 1);
        int var12 = var1.getBlockId(var2 + 1, var3, var4 + 1);
        int var13 = var1.getBlockId(var2 - 1, var3, var4 + 1);
        boolean var14 = var8 == this.blockID || var9 == this.blockID;
        boolean var15 = var6 == this.blockID || var7 == this.blockID;
        boolean var16 = var10 == this.blockID || var11 == this.blockID || var12 == this.blockID || var13 == this.blockID;

        for (int var17 = var2 - 1; var17 <= var2 + 1; ++var17) {
            for (int var18 = var4 - 1; var18 <= var4 + 1; ++var18) {
                int var19 = var1.getBlockId(var17, var3 - 1, var18);
                float var20 = 0.0F;
                if (var19 == Block.FARMLAND.blockID) {
                    var20 = 1.0F;
                    if (var1.getBlockMetadata(var17, var3 - 1, var18) > 0) {
                        var20 = 3.0F;
                    }
                }

                if (var17 != var2 || var18 != var4) {
                    var20 /= 4.0F;
                }

                var5 += var20;
            }
        }

        if (var16 || var14 && var15) {
            var5 /= 2.0F;
        }

        return var5;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        if (var2 < 0) {
            var2 = 7;
        }

        return this.blockIndexInTexture + var2;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int var5, float chance) {
        super.dropBlockAsItemWithChance(world, x, y, z, var5, chance);
        if (!world.singleplayerWorld) {
            for (int var7 = 0; var7 < 3; ++var7) {
                if (world.rand.nextInt(15) <= var5) {
                    float var8 = 0.7F;
                    float var9 = world.rand.nextFloat() * var8 + (1.0F - var8) * 0.5F;
                    float var10 = world.rand.nextFloat() * var8 + (1.0F - var8) * 0.5F;
                    float var11 = world.rand.nextFloat() * var8 + (1.0F - var8) * 0.5F;
                    EntityItem var12 = new EntityItem(world, (float) x + var9, (float) y + var10, (float) z + var11, new ItemStack(Item.SEEDS));
                    var12.delayBeforeCanPickup = 10;
                    world.entityJoinedWorld(var12);
                }
            }

        }
    }

    @Override
    public int idDropped(int var1, Random random) {
        return var1 == 7 ? Item.WHEAT.shiftedIndex : -1;
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
}
