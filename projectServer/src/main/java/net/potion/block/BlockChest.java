package net.potion.block;

import net.potion.entity.item.EntityItem;
import net.potion.entity.player.EntityPlayer;
import net.potion.inventory.IInventory;
import net.potion.inventory.InventoryLargeChest;
import net.potion.item.ItemStack;
import net.potion.material.Material;
import net.potion.tileentity.TileEntity;
import net.potion.tileentity.TileEntityChest;
import net.potion.world.World;

import java.util.Random;

public class BlockChest extends BlockContainer {
    private Random random = new Random();

    protected BlockChest(int var1) {
        super(var1, Material.WOOD);
        this.blockIndexInTexture = 26;
    }

    @Override
    public int getBlockTextureFromSide(int var1) {
        if (var1 == 1) {
            return this.blockIndexInTexture - 1;
        } else if (var1 == 0) {
            return this.blockIndexInTexture - 1;
        } else {
            return var1 == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        int var5 = 0;
        if (world.getBlockId(var2 - 1, var3, var4) == this.blockID) {
            ++var5;
        }

        if (world.getBlockId(var2 + 1, var3, var4) == this.blockID) {
            ++var5;
        }

        if (world.getBlockId(var2, var3, var4 - 1) == this.blockID) {
            ++var5;
        }

        if (world.getBlockId(var2, var3, var4 + 1) == this.blockID) {
            ++var5;
        }

        if (var5 > 1) {
            return false;
        } else if (this.isThereANeighborChest(world, var2 - 1, var3, var4)) {
            return false;
        } else if (this.isThereANeighborChest(world, var2 + 1, var3, var4)) {
            return false;
        } else if (this.isThereANeighborChest(world, var2, var3, var4 - 1)) {
            return false;
        } else {
            return !this.isThereANeighborChest(world, var2, var3, var4 + 1);
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
    public void onBlockRemoval(World world, int x, int y, int z) {
        TileEntityChest var5 = (TileEntityChest) world.getBlockTileEntity(x, y, z);

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
                    EntityItem var12 = new EntityItem(world, (float) x + var8, (float) y + var9, (float) z + var10, new ItemStack(var7.itemID, var11, var7.getItemDamage()));
                    float var13 = 0.05F;
                    var12.motionX = (float) this.random.nextGaussian() * var13;
                    var12.motionY = (float) this.random.nextGaussian() * var13 + 0.2F;
                    var12.motionZ = (float) this.random.nextGaussian() * var13;
                    world.entityJoinedWorld(var12);
                }
            }
        }

        super.onBlockRemoval(world, x, y, z);
    }

    @Override
    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        Object var6 = world.getBlockTileEntity(var2, var3, var4);
        if (world.isBlockNormalCube(var2, var3 + 1, var4)) {
            return true;
        } else if (world.getBlockId(var2 - 1, var3, var4) == this.blockID && world.isBlockNormalCube(var2 - 1, var3 + 1, var4)) {
            return true;
        } else if (world.getBlockId(var2 + 1, var3, var4) == this.blockID && world.isBlockNormalCube(var2 + 1, var3 + 1, var4)) {
            return true;
        } else if (world.getBlockId(var2, var3, var4 - 1) == this.blockID && world.isBlockNormalCube(var2, var3 + 1, var4 - 1)) {
            return true;
        } else if (world.getBlockId(var2, var3, var4 + 1) == this.blockID && world.isBlockNormalCube(var2, var3 + 1, var4 + 1)) {
            return true;
        } else {
            if (world.getBlockId(var2 - 1, var3, var4) == this.blockID) {
                var6 = new InventoryLargeChest("Large chest", (TileEntityChest) world.getBlockTileEntity(var2 - 1, var3, var4), (IInventory) var6);
            }

            if (world.getBlockId(var2 + 1, var3, var4) == this.blockID) {
                var6 = new InventoryLargeChest("Large chest", (IInventory) var6, (TileEntityChest) world.getBlockTileEntity(var2 + 1, var3, var4));
            }

            if (world.getBlockId(var2, var3, var4 - 1) == this.blockID) {
                var6 = new InventoryLargeChest("Large chest", (TileEntityChest) world.getBlockTileEntity(var2, var3, var4 - 1), (IInventory) var6);
            }

            if (world.getBlockId(var2, var3, var4 + 1) == this.blockID) {
                var6 = new InventoryLargeChest("Large chest", (IInventory) var6, (TileEntityChest) world.getBlockTileEntity(var2, var3, var4 + 1));
            }

            if (world.singleplayerWorld) {
                return true;
            } else {
                entityPlayer.displayGUIChest((IInventory) var6);
                return true;
            }
        }
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityChest();
    }
}
