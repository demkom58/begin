package net.potion.item;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.entity.player.EntityPlayer;
import net.potion.network.packet.Packet;
import net.potion.world.World;

public class ItemMapBase extends Item {
    protected ItemMapBase(int var1) {
        super(var1);
    }

    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }

    @Side(CodeSide.SERVER)
    public Packet method1(ItemStack var1, World var2, EntityPlayer var3) {
        return null;
    }

}
