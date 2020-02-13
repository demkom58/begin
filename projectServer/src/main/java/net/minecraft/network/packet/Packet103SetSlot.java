package net.minecraft.network.packet;

import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet103SetSlot extends Packet {
    public int windowId;
    public int itemSlot;
    public ItemStack myItemStack;

    public Packet103SetSlot() {
    }

    public Packet103SetSlot(int var1, int var2, ItemStack var3) {
        this.windowId = var1;
        this.itemSlot = var2;
        this.myItemStack = var3 == null ? var3 : var3.copy();
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.func_20003_a(this);
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.windowId = inputStream.readByte();
        this.itemSlot = inputStream.readShort();
        short var2 = inputStream.readShort();
        if (var2 >= 0) {
            byte var3 = inputStream.readByte();
            short var4 = inputStream.readShort();
            this.myItemStack = new ItemStack(var2, var3, var4);
        } else {
            this.myItemStack = null;
        }

    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeByte(this.windowId);
        outputStream.writeShort(this.itemSlot);
        if (this.myItemStack == null) {
            outputStream.writeShort(-1);
        } else {
            outputStream.writeShort(this.myItemStack.itemID);
            outputStream.writeByte(this.myItemStack.stackSize);
            outputStream.writeShort(this.myItemStack.getItemDamage());
        }

    }

    @Override
    public int getPacketSize() {
        return 8;
    }
}
