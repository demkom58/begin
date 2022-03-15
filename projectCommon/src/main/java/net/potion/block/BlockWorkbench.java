package net.potion.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.entity.player.EntityPlayer;
import net.potion.material.Material;
import net.potion.world.World;

public class BlockWorkbench extends Block {
    protected BlockWorkbench(int var1) {
        super(var1, Material.WOOD);
        this.blockIndexInTexture = 59;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getBlockTextureFromSide(int side) {
        if (side == 1) {
            return this.blockIndexInTexture - 16;
        } else if (side == 0) {
            return Block.PLANKS.getBlockTextureFromSide(0);
        } else {
            return side != 2 && side != 4 ? this.blockIndexInTexture : this.blockIndexInTexture + 1;
        }
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (!world.localWorld) {
            player.displayWorkbenchGUI(x, y, z);
        }

        return true;
    }
}
