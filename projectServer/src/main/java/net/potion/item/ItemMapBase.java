package net.potion.item;

import net.potion.entity.player.EntityPlayer;
import net.potion.network.packet.Packet;
import net.potion.world.World;

public class ItemMapBase extends Item {
    protected ItemMapBase(int var1) {
        super(var1);
    }

    @Override
    public boolean func_28019_b() {
        return true;
    }

    public Packet func_28022_b(ItemStack var1, World var2, EntityPlayer var3) {
        return null;
    }
}
