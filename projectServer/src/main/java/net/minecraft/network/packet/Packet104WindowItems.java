package net.minecraft.network.packet;

import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class Packet104WindowItems extends Packet {
    public int windowId;
    public ItemStack[] itemStack;

    public Packet104WindowItems() {
    }

    public Packet104WindowItems(int var1, List<ItemStack> var2) {
        this.windowId = var1;
        this.itemStack = new ItemStack[var2.size()];

        for (int var3 = 0; var3 < this.itemStack.length; ++var3) {
            ItemStack var4 = var2.get(var3);
            this.itemStack[var3] = var4 == null ? null : var4.copy();
        }

    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.windowId = inputStream.readByte();
        short var2 = inputStream.readShort();
        this.itemStack = new ItemStack[var2];

        for (int var3 = 0; var3 < var2; ++var3) {
            short var4 = inputStream.readShort();
            if (var4 >= 0) {
                byte var5 = inputStream.readByte();
                short var6 = inputStream.readShort();
                this.itemStack[var3] = new ItemStack(var4, var5, var6);
            }
        }

    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeByte(this.windowId);
        outputStream.writeShort(this.itemStack.length);

        for (int var2 = 0; var2 < this.itemStack.length; ++var2) {
            if (this.itemStack[var2] == null) {
                outputStream.writeShort(-1);
            } else {
                outputStream.writeShort((short) this.itemStack[var2].itemID);
                outputStream.writeByte((byte) this.itemStack[var2].stackSize);
                outputStream.writeShort((short) this.itemStack[var2].getItemDamage());
            }
        }

    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.func_20001_a(this);
    }

    @Override
    public int getPacketSize() {
        return 3 + this.itemStack.length * 5;
    }
}
