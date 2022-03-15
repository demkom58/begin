package net.potion.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.item.ItemStack;
import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet5PlayerInventory extends Packet {
    public int entityID;
    public int slot;
    public int itemID;
    public int itemDamage;


    public Packet5PlayerInventory() {
    }

    @Side(CodeSide.SERVER)
    public Packet5PlayerInventory(int var1, int var2, ItemStack var3) {
        this.entityID = var1;
        this.slot = var2;
        if (var3 == null) {
            this.itemID = -1;
            this.itemDamage = 0;
        } else {
            this.itemID = var3.itemID;
            this.itemDamage = var3.getItemDamage();
        }

    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.entityID = var1.readInt();
        this.slot = var1.readShort();
        this.itemID = var1.readShort();
        this.itemDamage = var1.readShort();
    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.entityID);
        var1.writeShort(this.slot);
        var1.writeShort(this.itemID);
        var1.writeShort(this.itemDamage);
    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handlePlayerInventory(this);
    }

    @Override
    public int getPacketSize() {
        return 8;
    }
}
