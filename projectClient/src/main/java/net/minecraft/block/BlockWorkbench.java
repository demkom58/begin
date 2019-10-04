package net.minecraft.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.material.Material;
import net.minecraft.world.World;

public class BlockWorkbench extends Block {
    protected BlockWorkbench(int var1) {
        super(var1, Material.WOOD);
        this.blockIndexInTexture = 59;
    }

    public int getBlockTextureFromSide(int side) {
        if (side == 1) {
            return this.blockIndexInTexture - 16;
        } else if (side == 0) {
            return Block.PLANKS.getBlockTextureFromSide(0);
        } else {
            return side != 2 && side != 4 ? this.blockIndexInTexture : this.blockIndexInTexture + 1;
        }
    }

    public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
        if (var1.multiplayerWorld) {
            return true;
        } else {
            var5.displayWorkbenchGUI(var2, var3, var4);
            return true;
        }
    }
}
