package net.potion.block;

import net.potion.entity.EntityLiving;
import net.potion.entity.item.EntityItem;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.ItemStack;
import net.potion.material.Material;
import net.potion.tileentity.TileEntity;
import net.potion.tileentity.TileEntityFurnace;
import net.hypnosis.util.math.MathHelper;
import net.potion.world.IBlockAccess;
import net.potion.world.World;

import java.util.Random;

public class BlockFurnace extends BlockContainer {
    private static boolean keepFurnaceInventory = false;
    private final boolean isActive;
    private Random furnaceRand = new Random();

    protected BlockFurnace(int var1, boolean var2) {
        super(var1, Material.ROCK);
        this.isActive = var2;
        this.blockIndexInTexture = 45;
    }

    public static void updateFurnaceBlockState(boolean var0, World var1, int var2, int var3, int var4) {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        TileEntity var6 = var1.getBlockTileEntity(var2, var3, var4);
        keepFurnaceInventory = true;
        if (var0) {
            var1.setBlockWithNotify(var2, var3, var4, Block.FURNACE_ACTIVE.blockID);
        } else {
            var1.setBlockWithNotify(var2, var3, var4, Block.FURNACE.blockID);
        }

        keepFurnaceInventory = false;
        var1.setBlockMetadataWithNotify(var2, var3, var4, var5);
        var6.func_31004_j();
        var1.setBlockTileEntity(var2, var3, var4, var6);
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return Block.FURNACE.blockID;
    }

    @Override
    public void onBlockAdded(World var1, int var2, int var3, int var4) {
        super.onBlockAdded(var1, var2, var3, var4);
        this.setDefaultDirection(var1, var2, var3, var4);
    }

    private void setDefaultDirection(World var1, int var2, int var3, int var4) {
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
    public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
        if (side == 1) {
            return this.blockIndexInTexture + 17;
        } else if (side == 0) {
            return this.blockIndexInTexture + 17;
        } else {
            int var6 = blockAccess.getBlockMetadata(x, y, z);
            if (side != var6) {
                return this.blockIndexInTexture;
            } else {
                return this.isActive ? this.blockIndexInTexture + 16 : this.blockIndexInTexture - 1;
            }
        }
    }

    @Override
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
        if (this.isActive) {
            int var6 = var1.getBlockMetadata(var2, var3, var4);
            float var7 = (float) var2 + 0.5F;
            float var8 = (float) var3 + 0.0F + var5.nextFloat() * 6.0F / 16.0F;
            float var9 = (float) var4 + 0.5F;
            float var10 = 0.52F;
            float var11 = var5.nextFloat() * 0.6F - 0.3F;
            if (var6 == 4) {
                var1.spawnParticle("smoke", var7 - var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
                var1.spawnParticle("flame", var7 - var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
            } else if (var6 == 5) {
                var1.spawnParticle("smoke", var7 + var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
                var1.spawnParticle("flame", var7 + var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
            } else if (var6 == 2) {
                var1.spawnParticle("smoke", var7 + var11, var8, var9 - var10, 0.0D, 0.0D, 0.0D);
                var1.spawnParticle("flame", var7 + var11, var8, var9 - var10, 0.0D, 0.0D, 0.0D);
            } else if (var6 == 3) {
                var1.spawnParticle("smoke", var7 + var11, var8, var9 + var10, 0.0D, 0.0D, 0.0D);
                var1.spawnParticle("flame", var7 + var11, var8, var9 + var10, 0.0D, 0.0D, 0.0D);
            }

        }
    }

    @Override
    public int getBlockTextureFromSide(int side) {
        if (side == 1) {
            return this.blockIndexInTexture + 17;
        } else if (side == 0) {
            return this.blockIndexInTexture + 17;
        } else {
            return side == 3 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture;
        }
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (world.multiplayerWorld) {
            return true;
        } else {
            TileEntityFurnace var6 = (TileEntityFurnace) world.getBlockTileEntity(x, y, z);
            player.displayGUIFurnace(var6);
            return true;
        }
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityFurnace();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity) {
        int var6 = MathHelper.floor((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
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
    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        if (!keepFurnaceInventory) {
            TileEntityFurnace var5 = (TileEntityFurnace) var1.getBlockTileEntity(var2, var3, var4);

            for (int var6 = 0; var6 < var5.getSizeInventory(); ++var6) {
                ItemStack var7 = var5.getStackInSlot(var6);
                if (var7 != null) {
                    float var8 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                    float var9 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                    float var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

                    while (var7.stackSize > 0) {
                        int var11 = this.furnaceRand.nextInt(21) + 10;
                        if (var11 > var7.stackSize) {
                            var11 = var7.stackSize;
                        }

                        var7.stackSize -= var11;
                        EntityItem var12 = new EntityItem(var1, (float) var2 + var8, (float) var3 + var9, (float) var4 + var10, new ItemStack(var7.itemID, var11, var7.getItemDamage()));
                        float var13 = 0.05F;
                        var12.motionX = (float) this.furnaceRand.nextGaussian() * var13;
                        var12.motionY = (float) this.furnaceRand.nextGaussian() * var13 + 0.2F;
                        var12.motionZ = (float) this.furnaceRand.nextGaussian() * var13;
                        var1.entityJoinedWorld(var12);
                    }
                }
            }
        }

        super.onBlockRemoval(var1, var2, var3, var4);
    }
}
