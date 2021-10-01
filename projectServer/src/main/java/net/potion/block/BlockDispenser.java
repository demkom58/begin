package net.potion.block;

import net.potion.entity.EntityLiving;
import net.potion.entity.item.EntityItem;
import net.potion.entity.player.EntityPlayer;
import net.potion.entity.projectile.EntityArrow;
import net.potion.entity.projectile.EntityEgg;
import net.potion.entity.projectile.EntitySnowball;
import net.potion.item.Item;
import net.potion.item.ItemStack;
import net.potion.material.Material;
import net.potion.tileentity.TileEntity;
import net.potion.tileentity.TileEntityDispenser;
import net.potion.util.MathHelper;
import net.potion.world.World;

import java.util.Random;

public class BlockDispenser extends BlockContainer {
    private Random field_28032_a = new Random();

    protected BlockDispenser(int var1) {
        super(var1, Material.ROCK);
        this.blockIndexInTexture = 45;
    }

    @Override
    public int tickRate() {
        return 4;
    }

    @Override
    public int idDropped(int var1, Random random) {
        return Block.DISPENSER.blockID;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        this.setDispenserDefaultDirection(world, x, y, z);
    }

    private void setDispenserDefaultDirection(World var1, int var2, int var3, int var4) {
        if (!var1.singleplayerWorld) {
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
    public int getBlockTextureFromSide(int var1) {
        if (var1 == 1) {
            return this.blockIndexInTexture + 17;
        } else if (var1 == 0) {
            return this.blockIndexInTexture + 17;
        } else {
            return var1 == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
        }
    }

    @Override
    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        if (world.singleplayerWorld) {
            return true;
        } else {
            TileEntityDispenser var6 = (TileEntityDispenser) world.getBlockTileEntity(var2, var3, var4);
            entityPlayer.displayGUIDispenser(var6);
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
            var1.func_28097_e(1001, var2, var3, var4, 0);
        } else {
            if (var12.itemID == Item.ARROW.shiftedIndex) {
                EntityArrow var19 = new EntityArrow(var1, var13, var15, var17);
                var19.setArrowHeading(var9, 0.10000000149011612D, var10, 1.1F, 6.0F);
                var19.field_28012_a = true;
                var1.entityJoinedWorld(var19);
                var1.func_28097_e(1002, var2, var3, var4, 0);
            } else if (var12.itemID == Item.EGG.shiftedIndex) {
                EntityEgg var22 = new EntityEgg(var1, var13, var15, var17);
                var22.func_20078_a(var9, 0.10000000149011612D, var10, 1.1F, 6.0F);
                var1.entityJoinedWorld(var22);
                var1.func_28097_e(1002, var2, var3, var4, 0);
            } else if (var12.itemID == Item.SNOWBALL.shiftedIndex) {
                EntitySnowball var23 = new EntitySnowball(var1, var13, var15, var17);
                var23.func_6141_a(var9, 0.10000000149011612D, var10, 1.1F, 6.0F);
                var1.entityJoinedWorld(var23);
                var1.func_28097_e(1002, var2, var3, var4, 0);
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
                var1.func_28097_e(1000, var2, var3, var4, 0);
            }

            var1.func_28097_e(2000, var2, var3, var4, var9 + 1 + (var10 + 1) * 3);
        }

    }

    @Override
    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        if (var5 > 0 && Block.BLOCKS_LIST[var5].canProvidePower()) {
            boolean var6 = world.isBlockIndirectlyGettingPowered(var2, var3, var4) || world.isBlockIndirectlyGettingPowered(var2, var3 + 1, var4);
            if (var6) {
                world.scheduleUpdateTick(var2, var3, var4, this.blockID, this.tickRate());
            }
        }

    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z)) {
            this.dispenseItem(world, x, y, z, random);
        }

    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityDispenser();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving) {
        int var6 = MathHelper.floor((double) (entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        if (var6 == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 2);
        }

        if (var6 == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 5);
        }

        if (var6 == 2) {
            world.setBlockMetadataWithNotify(x, y, z, 3);
        }

        if (var6 == 3) {
            world.setBlockMetadataWithNotify(x, y, z, 4);
        }

    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        TileEntityDispenser var5 = (TileEntityDispenser) world.getBlockTileEntity(x, y, z);

        for (int var6 = 0; var6 < var5.getSizeInventory(); ++var6) {
            ItemStack var7 = var5.getStackInSlot(var6);
            if (var7 != null) {
                float var8 = this.field_28032_a.nextFloat() * 0.8F + 0.1F;
                float var9 = this.field_28032_a.nextFloat() * 0.8F + 0.1F;
                float var10 = this.field_28032_a.nextFloat() * 0.8F + 0.1F;

                while (var7.stackSize > 0) {
                    int var11 = this.field_28032_a.nextInt(21) + 10;
                    if (var11 > var7.stackSize) {
                        var11 = var7.stackSize;
                    }

                    var7.stackSize -= var11;
                    EntityItem var12 = new EntityItem(world, (float) x + var8, (float) y + var9, (float) z + var10, new ItemStack(var7.itemID, var11, var7.getItemDamage()));
                    float var13 = 0.05F;
                    var12.motionX = (float) this.field_28032_a.nextGaussian() * var13;
                    var12.motionY = (float) this.field_28032_a.nextGaussian() * var13 + 0.2F;
                    var12.motionZ = (float) this.field_28032_a.nextGaussian() * var13;
                    world.entityJoinedWorld(var12);
                }
            }
        }

        super.onBlockRemoval(world, x, y, z);
    }
}
