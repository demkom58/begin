package net.potion.block;

import net.potion.entity.player.EntityPlayer;
import net.potion.item.Item;
import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.util.MovingObjectPosition;
import net.potion.world.IBlockAccess;
import net.potion.world.World;
import org.joml.Vector3d;

import java.util.Random;

public class BlockDoor extends Block {
    protected BlockDoor(int var1, Material var2) {
        super(var1, var2);
        this.blockIndexInTexture = 97;
        if (var2 == Material.IRON) {
            ++this.blockIndexInTexture;
        }

        float var3 = 0.5F;
        float var4 = 1.0F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var4, 0.5F + var3);
    }

    public static boolean isOpen(int var0) {
        return (var0 & 4) != 0;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        if (side != 0 && side != 1) {
            int var3 = this.getState(metadata);
            if ((var3 == 0 || var3 == 2) ^ side <= 3) {
                return this.blockIndexInTexture;
            } else {
                int var4 = var3 / 2 + (side & 1 ^ var3);
                var4 = var4 + (metadata & 4) / 4;
                int var5 = this.blockIndexInTexture - (metadata & 8) * 2;
                if ((var4 & 1) != 0) {
                    var5 = -var5;
                }

                return var5;
            }
        } else {
            return this.blockIndexInTexture;
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 7;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        this.setBlockBoundsBasedOnState(var1, var2, var3, var4);
        return super.getCollisionBoundingBoxFromPool(var1, var2, var3, var4);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        this.setDoorRotation(this.getState(blockAccess.getBlockMetadata(x, y, z)));
    }

    public void setDoorRotation(int var1) {
        float var2 = 0.1875F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
        if (var1 == 0) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
        }

        if (var1 == 1) {
            this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if (var1 == 2) {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
        }

        if (var1 == 3) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
        }

    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        this.blockActivated(world, x, y, z, player);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (this.blockMaterial == Material.IRON) {
            return true;
        } else {
            int var6 = world.getBlockMetadata(x, y, z);
            if ((var6 & 8) != 0) {
                if (world.getBlockId(x, y - 1, z) == this.blockID) {
                    this.blockActivated(world, x, y - 1, z, player);
                }

                return true;
            } else {
                if (world.getBlockId(x, y + 1, z) == this.blockID) {
                    world.setBlockMetadataWithNotify(x, y + 1, z, (var6 ^ 4) + 8);
                }

                world.setBlockMetadataWithNotify(x, y, z, var6 ^ 4);
                world.markBlocksDirty(x, y - 1, z, x, y, z);
                world.playEffects(player, 1003, x, y, z, 0);
                return true;
            }
        }
    }

    public void onPoweredBlockChange(World world, int x, int y, int z, boolean activate) {
        int blockMetadata = world.getBlockMetadata(x, y, z);
        if ((blockMetadata & 8) != 0) {
            if (world.getBlockId(x, y - 1, z) == this.blockID) {
                this.onPoweredBlockChange(world, x, y - 1, z, activate);
            }

            return;
        }

        boolean isActivated = (world.getBlockMetadata(x, y, z) & 4) > 0;
        if (isActivated != activate) {
            if (world.getBlockId(x, y + 1, z) == this.blockID)
                world.setBlockMetadataWithNotify(x, y + 1, z, (blockMetadata ^ 4) + 8);

            world.setBlockMetadataWithNotify(x, y, z, blockMetadata ^ 4);
            world.markBlocksDirty(x, y - 1, z, x, y, z);
            world.playEffects(null, 1003, x, y, z, 0);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int var5) {
        int blockMetadata = world.getBlockMetadata(x, y, z);
        if ((blockMetadata & 8) != 0) {
            if (world.getBlockId(x, y - 1, z) != this.blockID) {
                world.setBlockWithNotify(x, y, z, 0);
            }

            if (var5 > 0 && Block.BLOCKS_LIST[var5].canProvidePower()) {
                this.onNeighborBlockChange(world, x, y - 1, z, var5);
            }
            return;
        }

        boolean var7 = false;
        if (world.getBlockId(x, y + 1, z) != this.blockID) {
            world.setBlockWithNotify(x, y, z, 0);
            var7 = true;
        }

        if (!world.isBlockNormalCube(x, y - 1, z)) {
            world.setBlockWithNotify(x, y, z, 0);
            var7 = true;
            if (world.getBlockId(x, y + 1, z) == this.blockID)
                world.setBlockWithNotify(x, y + 1, z, 0);
        }

        if (var7) {
            if (!world.multiplayerWorld)
                this.dropBlockAsItem(world, x, y, z, blockMetadata);
        } else if (var5 > 0 && Block.BLOCKS_LIST[var5].canProvidePower()) {
            boolean var8 = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);
            this.onPoweredBlockChange(world, x, y, z, var8);
        }
    }

    @Override
    public int idDropped(int var1, Random random) {
        if ((var1 & 8) != 0)
            return 0;

        return this.blockMaterial == Material.IRON ? Item.DOOR_IRON.shiftedIndex : Item.DOOR_WOOD.shiftedIndex;
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vector3d var5, Vector3d var6) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.collisionRayTrace(world, x, y, z, var5, var6);
    }

    public int getState(int var1) {
        return (var1 & 4) == 0 ? var1 - 1 & 3 : var1 & 3;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        if (y >= 127)
            return false;

        return world.isBlockNormalCube(x, y - 1, z) && super.canPlaceBlockAt(world, x, y, z) && super.canPlaceBlockAt(world, x, y + 1, z);
    }

    @Override
    public int getMobilityFlag() {
        return 1;
    }
}
