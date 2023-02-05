package net.minecraft.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockChest extends BlockContainer {
    private final Random random = new Random();

    protected BlockChest(int var1) {
        super(var1, Material.WOOD);
        this.blockIndexInTexture = 26;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
        if (side == 1) {
            return this.blockIndexInTexture - 1;
        }

        if (side == 0) {
            return this.blockIndexInTexture - 1;
        }

        int var6 = blockAccess.getBlockId(x, y, z - 1);
        int var7 = blockAccess.getBlockId(x, y, z + 1);
        int var8 = blockAccess.getBlockId(x - 1, y, z);
        int var9 = blockAccess.getBlockId(x + 1, y, z);
        if (var6 != this.blockID && var7 != this.blockID) {
            if (var8 != this.blockID && var9 != this.blockID) {
                byte var15 = 3;
                if (Block.OPAQUE_CUBE_LOOKUP[var6] && !Block.OPAQUE_CUBE_LOOKUP[var7]) {
                    var15 = 3;
                }

                if (Block.OPAQUE_CUBE_LOOKUP[var7] && !Block.OPAQUE_CUBE_LOOKUP[var6]) {
                    var15 = 2;
                }

                if (Block.OPAQUE_CUBE_LOOKUP[var8] && !Block.OPAQUE_CUBE_LOOKUP[var9]) {
                    var15 = 5;
                }

                if (Block.OPAQUE_CUBE_LOOKUP[var9] && !Block.OPAQUE_CUBE_LOOKUP[var8]) {
                    var15 = 4;
                }

                return side == var15 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
            } else if (side != 4 && side != 5) {
                int var14 = 0;
                if (var8 == this.blockID) {
                    var14 = -1;
                }

                int var16 = blockAccess.getBlockId(var8 == this.blockID ? x - 1 : x + 1, y, z - 1);
                int var17 = blockAccess.getBlockId(var8 == this.blockID ? x - 1 : x + 1, y, z + 1);
                if (side == 3) {
                    var14 = -1 - var14;
                }

                byte var18 = 3;
                if ((Block.OPAQUE_CUBE_LOOKUP[var6] || Block.OPAQUE_CUBE_LOOKUP[var16]) && !Block.OPAQUE_CUBE_LOOKUP[var7] && !Block.OPAQUE_CUBE_LOOKUP[var17]) {
                    var18 = 3;
                }

                if ((Block.OPAQUE_CUBE_LOOKUP[var7] || Block.OPAQUE_CUBE_LOOKUP[var17]) && !Block.OPAQUE_CUBE_LOOKUP[var6] && !Block.OPAQUE_CUBE_LOOKUP[var16]) {
                    var18 = 2;
                }

                return (side == var18 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + var14;
            } else {
                return this.blockIndexInTexture;
            }
        } else if (side != 2 && side != 3) {
            int var10 = 0;
            if (var6 == this.blockID) {
                var10 = -1;
            }

            int var11 = blockAccess.getBlockId(x - 1, y, var6 == this.blockID ? z - 1 : z + 1);
            int var12 = blockAccess.getBlockId(x + 1, y, var6 == this.blockID ? z - 1 : z + 1);
            if (side == 4) {
                var10 = -1 - var10;
            }

            byte var13 = 5;
            if ((Block.OPAQUE_CUBE_LOOKUP[var8] || Block.OPAQUE_CUBE_LOOKUP[var11]) && !Block.OPAQUE_CUBE_LOOKUP[var9] && !Block.OPAQUE_CUBE_LOOKUP[var12]) {
                var13 = 5;
            }

            if ((Block.OPAQUE_CUBE_LOOKUP[var9] || Block.OPAQUE_CUBE_LOOKUP[var12]) && !Block.OPAQUE_CUBE_LOOKUP[var8] && !Block.OPAQUE_CUBE_LOOKUP[var11]) {
                var13 = 4;
            }

            return (side == var13 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + var10;
        } else {
            return this.blockIndexInTexture;
        }
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getBlockTextureFromSide(int side) {
        if (side == 1) {
            return this.blockIndexInTexture - 1;
        } else if (side == 0) {
            return this.blockIndexInTexture - 1;
        } else {
            return side == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        int var5 = 0;
        if (world.getBlockId(x - 1, y, z) == this.blockID) {
            ++var5;
        }

        if (world.getBlockId(x + 1, y, z) == this.blockID) {
            ++var5;
        }

        if (world.getBlockId(x, y, z - 1) == this.blockID) {
            ++var5;
        }

        if (world.getBlockId(x, y, z + 1) == this.blockID) {
            ++var5;
        }

        if (var5 > 1) {
            return false;
        } else if (this.isThereANeighborChest(world, x - 1, y, z)) {
            return false;
        } else if (this.isThereANeighborChest(world, x + 1, y, z)) {
            return false;
        } else if (this.isThereANeighborChest(world, x, y, z - 1)) {
            return false;
        } else {
            return !this.isThereANeighborChest(world, x, y, z + 1);
        }
    }

    private boolean isThereANeighborChest(World var1, int var2, int var3, int var4) {
        if (var1.getBlockId(var2, var3, var4) != this.blockID) {
            return false;
        } else if (var1.getBlockId(var2 - 1, var3, var4) == this.blockID) {
            return true;
        } else if (var1.getBlockId(var2 + 1, var3, var4) == this.blockID) {
            return true;
        } else if (var1.getBlockId(var2, var3, var4 - 1) == this.blockID) {
            return true;
        } else {
            return var1.getBlockId(var2, var3, var4 + 1) == this.blockID;
        }
    }

    @Override
    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        TileEntityChest var5 = (TileEntityChest) var1.getBlockTileEntity(var2, var3, var4);

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

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        Object var6 = world.getBlockTileEntity(x, y, z);
        if (world.isBlockNormalCube(x, y + 1, z)) {
            return true;
        } else if (world.getBlockId(x - 1, y, z) == this.blockID && world.isBlockNormalCube(x - 1, y + 1, z)) {
            return true;
        } else if (world.getBlockId(x + 1, y, z) == this.blockID && world.isBlockNormalCube(x + 1, y + 1, z)) {
            return true;
        } else if (world.getBlockId(x, y, z - 1) == this.blockID && world.isBlockNormalCube(x, y + 1, z - 1)) {
            return true;
        } else if (world.getBlockId(x, y, z + 1) == this.blockID && world.isBlockNormalCube(x, y + 1, z + 1)) {
            return true;
        } else {
            if (world.getBlockId(x - 1, y, z) == this.blockID) {
                var6 = new InventoryLargeChest("Large chest", (TileEntityChest) world.getBlockTileEntity(x - 1, y, z), (IInventory) var6);
            }

            if (world.getBlockId(x + 1, y, z) == this.blockID) {
                var6 = new InventoryLargeChest("Large chest", (IInventory) var6, (TileEntityChest) world.getBlockTileEntity(x + 1, y, z));
            }

            if (world.getBlockId(x, y, z - 1) == this.blockID) {
                var6 = new InventoryLargeChest("Large chest", (TileEntityChest) world.getBlockTileEntity(x, y, z - 1), (IInventory) var6);
            }

            if (world.getBlockId(x, y, z + 1) == this.blockID) {
                var6 = new InventoryLargeChest("Large chest", (IInventory) var6, (TileEntityChest) world.getBlockTileEntity(x, y, z + 1));
            }

            if (world.localWorld) {
                return true;
            } else {
                player.displayGUIChest((IInventory) var6);
                return true;
            }
        }
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityChest();
    }
}
