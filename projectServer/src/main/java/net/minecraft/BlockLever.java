package net.minecraft;

public class BlockLever extends Block {
    protected BlockLever(int var1, int var2) {
        super(var1, var2, Material.circuits);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isACube() {
        return false;
    }

    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int var5) {
        if (var5 == 1 && world.isBlockNormalCube(x, y - 1, z)) {
            return true;
        } else if (var5 == 2 && world.isBlockNormalCube(x, y, z + 1)) {
            return true;
        } else if (var5 == 3 && world.isBlockNormalCube(x, y, z - 1)) {
            return true;
        } else if (var5 == 4 && world.isBlockNormalCube(x + 1, y, z)) {
            return true;
        } else {
            return var5 == 5 && world.isBlockNormalCube(x - 1, y, z);
        }
    }

    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        if (world.isBlockNormalCube(var2 - 1, var3, var4)) {
            return true;
        } else if (world.isBlockNormalCube(var2 + 1, var3, var4)) {
            return true;
        } else if (world.isBlockNormalCube(var2, var3, var4 - 1)) {
            return true;
        } else if (world.isBlockNormalCube(var2, var3, var4 + 1)) {
            return true;
        } else {
            return world.isBlockNormalCube(var2, var3 - 1, var4);
        }
    }

    public void onBlockPlaced(World world, int var2, int var3, int var4, int var5) {
        int var6 = world.getBlockMetadata(var2, var3, var4);
        int var7 = var6 & 8;
        var6 = var6 & 7;
        var6 = -1;
        if (var5 == 1 && world.isBlockNormalCube(var2, var3 - 1, var4)) {
            var6 = 5 + world.rand.nextInt(2);
        }

        if (var5 == 2 && world.isBlockNormalCube(var2, var3, var4 + 1)) {
            var6 = 4;
        }

        if (var5 == 3 && world.isBlockNormalCube(var2, var3, var4 - 1)) {
            var6 = 3;
        }

        if (var5 == 4 && world.isBlockNormalCube(var2 + 1, var3, var4)) {
            var6 = 2;
        }

        if (var5 == 5 && world.isBlockNormalCube(var2 - 1, var3, var4)) {
            var6 = 1;
        }

        if (var6 == -1) {
            this.dropBlockAsItem(world, var2, var3, var4, world.getBlockMetadata(var2, var3, var4));
            world.setBlockWithNotify(var2, var3, var4, 0);
        } else {
            world.setBlockMetadataWithNotify(var2, var3, var4, var6 + var7);
        }
    }

    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        if (this.checkIfAttachedToBlock(world, var2, var3, var4)) {
            int var6 = world.getBlockMetadata(var2, var3, var4) & 7;
            boolean var7 = false;
            if (!world.isBlockNormalCube(var2 - 1, var3, var4) && var6 == 1) {
                var7 = true;
            }

            if (!world.isBlockNormalCube(var2 + 1, var3, var4) && var6 == 2) {
                var7 = true;
            }

            if (!world.isBlockNormalCube(var2, var3, var4 - 1) && var6 == 3) {
                var7 = true;
            }

            if (!world.isBlockNormalCube(var2, var3, var4 + 1) && var6 == 4) {
                var7 = true;
            }

            if (!world.isBlockNormalCube(var2, var3 - 1, var4) && var6 == 5) {
                var7 = true;
            }

            if (!world.isBlockNormalCube(var2, var3 - 1, var4) && var6 == 6) {
                var7 = true;
            }

            if (var7) {
                this.dropBlockAsItem(world, var2, var3, var4, world.getBlockMetadata(var2, var3, var4));
                world.setBlockWithNotify(var2, var3, var4, 0);
            }
        }

    }

    private boolean checkIfAttachedToBlock(World var1, int var2, int var3, int var4) {
        if (!this.canPlaceBlockAt(var1, var2, var3, var4)) {
            this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
            var1.setBlockWithNotify(var2, var3, var4, 0);
            return false;
        } else {
            return true;
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int var2, int var3, int var4) {
        int var5 = blockAccess.getBlockMetadata(var2, var3, var4) & 7;
        float var6 = 0.1875F;
        if (var5 == 1) {
            this.setBlockBounds(0.0F, 0.2F, 0.5F - var6, var6 * 2.0F, 0.8F, 0.5F + var6);
        } else if (var5 == 2) {
            this.setBlockBounds(1.0F - var6 * 2.0F, 0.2F, 0.5F - var6, 1.0F, 0.8F, 0.5F + var6);
        } else if (var5 == 3) {
            this.setBlockBounds(0.5F - var6, 0.2F, 0.0F, 0.5F + var6, 0.8F, var6 * 2.0F);
        } else if (var5 == 4) {
            this.setBlockBounds(0.5F - var6, 0.2F, 1.0F - var6 * 2.0F, 0.5F + var6, 0.8F, 1.0F);
        } else {
            var6 = 0.25F;
            this.setBlockBounds(0.5F - var6, 0.0F, 0.5F - var6, 0.5F + var6, 0.6F, 0.5F + var6);
        }

    }

    public void onBlockClicked(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        this.blockActivated(world, var2, var3, var4, entityPlayer);
    }

    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        if (world.singleplayerWorld) {
            return true;
        } else {
            int var6 = world.getBlockMetadata(var2, var3, var4);
            int var7 = var6 & 7;
            int var8 = 8 - (var6 & 8);
            world.setBlockMetadataWithNotify(var2, var3, var4, var7 + var8);
            world.markBlocksDirty(var2, var3, var4, var2, var3, var4);
            world.playSoundEffect((double) var2 + 0.5D, (double) var3 + 0.5D, (double) var4 + 0.5D, "random.click", 0.3F, var8 > 0 ? 0.6F : 0.5F);
            world.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
            if (var7 == 1) {
                world.notifyBlocksOfNeighborChange(var2 - 1, var3, var4, this.blockID);
            } else if (var7 == 2) {
                world.notifyBlocksOfNeighborChange(var2 + 1, var3, var4, this.blockID);
            } else if (var7 == 3) {
                world.notifyBlocksOfNeighborChange(var2, var3, var4 - 1, this.blockID);
            } else if (var7 == 4) {
                world.notifyBlocksOfNeighborChange(var2, var3, var4 + 1, this.blockID);
            } else {
                world.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
            }

            return true;
        }
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        int var5 = world.getBlockMetadata(x, y, z);
        if ((var5 & 8) > 0) {
            world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
            int var6 = var5 & 7;
            if (var6 == 1) {
                world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
            } else if (var6 == 2) {
                world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
            } else if (var6 == 3) {
                world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
            } else if (var6 == 4) {
                world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
            } else {
                world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            }
        }

        super.onBlockRemoval(world, x, y, z);
    }

    public boolean isPoweringTo(IBlockAccess blockAccess, int var2, int var3, int var4, int var5) {
        return (blockAccess.getBlockMetadata(var2, var3, var4) & 8) > 0;
    }

    public boolean isIndirectlyPoweringTo(World world, int var2, int var3, int var4, int var5) {
        int var6 = world.getBlockMetadata(var2, var3, var4);
        if ((var6 & 8) == 0) {
            return false;
        } else {
            int var7 = var6 & 7;
            if (var7 == 6 && var5 == 1) {
                return true;
            } else if (var7 == 5 && var5 == 1) {
                return true;
            } else if (var7 == 4 && var5 == 2) {
                return true;
            } else if (var7 == 3 && var5 == 3) {
                return true;
            } else if (var7 == 2 && var5 == 4) {
                return true;
            } else {
                return var7 == 1 && var5 == 5;
            }
        }
    }

    public boolean canProvidePower() {
        return true;
    }
}
