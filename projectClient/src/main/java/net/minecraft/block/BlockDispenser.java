package net.minecraft.block;

import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.MathHelper;

import java.util.Random;

public class BlockDispenser extends BlockContainer {
    private Random random = new Random();

    protected BlockDispenser(int var1) {
        super(var1, Material.ROCK);
        this.blockIndexInTexture = 45;
    }

    @Override
    public int tickRate() {
        return 4;
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return Block.DISPENSER.blockID;
    }

    @Override
    public void onBlockAdded(World var1, int var2, int var3, int var4) {
        super.onBlockAdded(var1, var2, var3, var4);
        this.setDispenserDefaultDirection(var1, var2, var3, var4);
    }

    private void setDispenserDefaultDirection(World var1, int var2, int var3, int var4) {
        if (!var1.multiplayerWorld) {
            int var5 = var1.getBlockId(var2, var3, var4 - 1);
            int var6 = var1.getBlockId(var2, var3, var4 + 1);
            int var7 = var1.getBlockId(var2 - 1, var3, var4);
            int var8 = var1.getBlockId(var2 + 1, var3, var4);
            byte var9 = 3;
            if (Block.OPAQUE_CUBE_LOOKUP[var5] && !Block.OPAQUE_CUBE_LOOKUP[var6]) {
                var9 = 3;
            }

            if (Block.OPAQUE_CUBE_LOOKUP[var6] && !Block.OPAQUE_CUBE_LOOKUP[var5]) {
                var9 = 2;
            }

            if (Block.OPAQUE_CUBE_LOOKUP[var7] && !Block.OPAQUE_CUBE_LOOKUP[var8]) {
                var9 = 5;
            }

            if (Block.OPAQUE_CUBE_LOOKUP[var8] && !Block.OPAQUE_CUBE_LOOKUP[var7]) {
                var9 = 4;
            }

            var1.setBlockMetadataWithNotify(var2, var3, var4, var9);
        }
    }

    @Override
    public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5) {
        if (var5 == 1) {
            return this.blockIndexInTexture + 17;
        } else if (var5 == 0) {
            return this.blockIndexInTexture + 17;
        } else {
            int var6 = var1.getBlockMetadata(var2, var3, var4);
            return var5 != var6 ? this.blockIndexInTexture : this.blockIndexInTexture + 1;
        }
    }

    @Override
    public int getBlockTextureFromSide(int side) {
        if (side == 1) {
            return this.blockIndexInTexture + 17;
        } else if (side == 0) {
            return this.blockIndexInTexture + 17;
        } else {
            return side == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
        }
    }

    @Override
    public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
        if (var1.multiplayerWorld) {
            return true;
        } else {
            TileEntityDispenser var6 = (TileEntityDispenser) var1.getBlockTileEntity(var2, var3, var4);
            var5.displayGUIDispenser(var6);
            return true;
        }
    }

    private void dispenseItem(World var1, int var2, int var3, int var4, Random var5) {
        int var6 = var1.getBlockMetadata(var2, var3, var4);
        byte var9 = 0;
        byte var10 = 0;
        if (var6 == 3) {
            var10 = 1;
        } else if (var6 == 2) {
            var10 = -1;
        } else if (var6 == 5) {
            var9 = 1;
        } else {
            var9 = -1;
        }

        TileEntityDispenser var11 = (TileEntityDispenser) var1.getBlockTileEntity(var2, var3, var4);
        ItemStack var12 = var11.getRandomStackFromInventory();
        double var13 = (double) var2 + (double) var9 * 0.6D + 0.5D;
        double var15 = (double) var3 + 0.5D;
        double var17 = (double) var4 + (double) var10 * 0.6D + 0.5D;
        if (var12 == null) {
            var1.func_28106_e(1001, var2, var3, var4, 0);
        } else {
            if (var12.itemID == Item.ARROW.shiftedIndex) {
                EntityArrow var19 = new EntityArrow(var1, var13, var15, var17);
                var19.setArrowHeading(var9, 0.10000000149011612D, var10, 1.1F, 6.0F);
                var19.doesArrowBelongToPlayer = true;
                var1.entityJoinedWorld(var19);
                var1.func_28106_e(1002, var2, var3, var4, 0);
            } else if (var12.itemID == Item.EGG.shiftedIndex) {
                EntityEgg var22 = new EntityEgg(var1, var13, var15, var17);
                var22.setEggHeading(var9, 0.10000000149011612D, var10, 1.1F, 6.0F);
                var1.entityJoinedWorld(var22);
                var1.func_28106_e(1002, var2, var3, var4, 0);
            } else if (var12.itemID == Item.SNOWBALL.shiftedIndex) {
                EntitySnowball var23 = new EntitySnowball(var1, var13, var15, var17);
                var23.setSnowballHeading(var9, 0.10000000149011612D, var10, 1.1F, 6.0F);
                var1.entityJoinedWorld(var23);
                var1.func_28106_e(1002, var2, var3, var4, 0);
            } else {
                EntityItem var24 = new EntityItem(var1, var13, var15 - 0.3D, var17, var12);
                double var20 = var5.nextDouble() * 0.1D + 0.2D;
                var24.motionX = (double) var9 * var20;
                var24.motionY = 0.20000000298023224D;
                var24.motionZ = (double) var10 * var20;
                var24.motionX += var5.nextGaussian() * 0.007499999832361937D * 6.0D;
                var24.motionY += var5.nextGaussian() * 0.007499999832361937D * 6.0D;
                var24.motionZ += var5.nextGaussian() * 0.007499999832361937D * 6.0D;
                var1.entityJoinedWorld(var24);
                var1.func_28106_e(1000, var2, var3, var4, 0);
            }

            var1.func_28106_e(2000, var2, var3, var4, var9 + 1 + (var10 + 1) * 3);
        }

    }

    @Override
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
        if (var5 > 0 && Block.BLOCKS_LIST[var5].canProvidePower()) {
            boolean var6 = var1.isBlockIndirectlyGettingPowered(var2, var3, var4) || var1.isBlockIndirectlyGettingPowered(var2, var3 + 1, var4);
            if (var6) {
                var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate());
            }
        }

    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        if (var1.isBlockIndirectlyGettingPowered(var2, var3, var4) || var1.isBlockIndirectlyGettingPowered(var2, var3 + 1, var4)) {
            this.dispenseItem(var1, var2, var3, var4, var5);
        }

    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityDispenser();
    }

    @Override
    public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5) {
        int var6 = MathHelper.floor((double) (var5.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        if (var6 == 0) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 2);
        }

        if (var6 == 1) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 5);
        }

        if (var6 == 2) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 3);
        }

        if (var6 == 3) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 4);
        }

    }

    @Override
    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        TileEntityDispenser var5 = (TileEntityDispenser) var1.getBlockTileEntity(var2, var3, var4);

        for (int var6 = 0; var6 < var5.getSizeInventory(); ++var6) {
            ItemStack var7 = var5.getStackInSlot(var6);
            if (var7 != null) {
                float var8 = this.random.nextFloat() * 0.8F + 0.1F;
                float var9 = this.random.nextFloat() * 0.8F + 0.1F;
                float var10 = this.random.nextFloat() * 0.8F + 0.1F;

                while (var7.stackSize > 0) {
                    int var11 = this.random.nextInt(21) + 10;
                    if (var11 > var7.stackSize) {
                        var11 = var7.stackSize;
                    }

                    var7.stackSize -= var11;
                    EntityItem var12 = new EntityItem(var1, (float) var2 + var8, (float) var3 + var9, (float) var4 + var10, new ItemStack(var7.itemID, var11, var7.getItemDamage()));
                    float var13 = 0.05F;
                    var12.motionX = (float) this.random.nextGaussian() * var13;
                    var12.motionY = (float) this.random.nextGaussian() * var13 + 0.2F;
                    var12.motionZ = (float) this.random.nextGaussian() * var13;
                    var1.entityJoinedWorld(var12);
                }
            }
        }

        super.onBlockRemoval(var1, var2, var3, var4);
    }
}
