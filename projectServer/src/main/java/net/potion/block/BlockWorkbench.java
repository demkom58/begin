package net.potion.block;

import net.potion.entity.player.EntityPlayer;
import net.potion.material.Material;
import net.potion.world.World;

public class BlockWorkbench extends Block {
    protected BlockWorkbench(int var1) {
        super(var1, Material.WOOD);
        this.blockIndexInTexture = 59;
    }

    @Override
    public int getBlockTextureFromSide(int var1) {
        if (var1 == 1) {
            return this.blockIndexInTexture - 16;
        } else if (var1 == 0) {
            return Block.PLANKS.getBlockTextureFromSide(0);
        } else {
            return var1 != 2 && var1 != 4 ? this.blockIndexInTexture : this.blockIndexInTexture + 1;
        }
    }

    @Override
    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        if (world.singleplayerWorld) {
            return true;
        } else {
            entityPlayer.displayWorkbenchGUI(var2, var3, var4);
            return true;
        }
    }
}
