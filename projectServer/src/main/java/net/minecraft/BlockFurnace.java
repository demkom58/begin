package net.minecraft;

import util.MathHelper;

import java.util.Random;

public class BlockFurnace extends BlockContainer {
    private static boolean field_28034_c = false;
    private final boolean isActive;
    private Random field_28033_a = new Random();

    protected BlockFurnace(int var1, boolean var2) {
        super(var1, Material.ROCK);
        this.isActive = var2;
        this.blockIndexInTexture = 45;
    }

    public static void updateFurnaceBlockState(boolean var0, World var1, int var2, int var3, int var4) {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        TileEntity var6 = var1.getBlockTileEntity(var2, var3, var4);
        field_28034_c = true;
        if (var0) {
            var1.setBlockWithNotify(var2, var3, var4, Block.FURNACE_ACTIVE.blockID);
        } else {
            var1.setBlockWithNotify(var2, var3, var4, Block.FURNACE.blockID);
        }

        field_28034_c = false;
        var1.setBlockMetadataWithNotify(var2, var3, var4, var5);
        var6.validate();
        var1.setBlockTileEntity(var2, var3, var4, var6);
    }

    public int idDropped(int var1, Random random) {
        return Block.FURNACE.blockID;
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        this.setDefaultDirection(world, x, y, z);
    }

    private void setDefaultDirection(World var1, int var2, int var3, int var4) {
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

    public int getBlockTextureFromSide(int var1) {
        if (var1 == 1) {
            return this.blockIndexInTexture + 17;
        } else if (var1 == 0) {
            return this.blockIndexInTexture + 17;
        } else {
            return var1 == 3 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture;
        }
    }

    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        if (world.singleplayerWorld) {
            return true;
        } else {
            TileEntityFurnace var6 = (TileEntityFurnace) world.getBlockTileEntity(var2, var3, var4);
            entityPlayer.displayGUIFurnace(var6);
            return true;
        }
    }

    protected TileEntity getBlockEntity() {
        return new TileEntityFurnace();
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving) {
        int var6 = MathHelper.floor_double((double) (entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
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

    public void onBlockRemoval(World world, int x, int y, int z) {
        if (!field_28034_c) {
            TileEntityFurnace var5 = (TileEntityFurnace) world.getBlockTileEntity(x, y, z);

            for (int var6 = 0; var6 < var5.getSizeInventory(); ++var6) {
                ItemStack var7 = var5.getStackInSlot(var6);
                if (var7 != null) {
                    float var8 = this.field_28033_a.nextFloat() * 0.8F + 0.1F;
                    float var9 = this.field_28033_a.nextFloat() * 0.8F + 0.1F;
                    float var10 = this.field_28033_a.nextFloat() * 0.8F + 0.1F;

                    while (var7.stackSize > 0) {
                        int var11 = this.field_28033_a.nextInt(21) + 10;
                        if (var11 > var7.stackSize) {
                            var11 = var7.stackSize;
                        }

                        var7.stackSize -= var11;
                        EntityItem var12 = new EntityItem(world, (double) ((float) x + var8), (double) ((float) y + var9), (double) ((float) z + var10), new ItemStack(var7.itemID, var11, var7.getItemDamage()));
                        float var13 = 0.05F;
                        var12.motionX = (double) ((float) this.field_28033_a.nextGaussian() * var13);
                        var12.motionY = (double) ((float) this.field_28033_a.nextGaussian() * var13 + 0.2F);
                        var12.motionZ = (double) ((float) this.field_28033_a.nextGaussian() * var13);
                        world.entityJoinedWorld(var12);
                    }
                }
            }
        }

        super.onBlockRemoval(world, x, y, z);
    }
}
