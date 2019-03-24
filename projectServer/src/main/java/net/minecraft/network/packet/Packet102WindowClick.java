package net.minecraft.network.packet;

import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet102WindowClick extends Packet {
    public int window_Id;
    public int inventorySlot;
    public int mouseClick;
    public short action;
    public ItemStack itemStack;
    public boolean field_27039_f;

    public void processPacket(NetHandler netHandler) {
        netHandler.func_20007_a(this);
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.window_Id = inputStream.readByte();
        this.inventorySlot = inputStream.readShort();
        this.mouseClick = inputStream.readByte();
        this.action = inputStream.readShort();
        this.field_27039_f = inputStream.readBoolean();
        short var2 = inputStream.readShort();
        if (var2 >= 0) {
            byte var3 = inputStream.readByte();
            short var4 = inputStream.readShort();
            this.itemStack = new ItemStack(var2, var3, var4);
        } else {
            this.itemStack = null;
        }

    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeByte(this.window_Id);
        outputStream.writeShort(this.inventorySlot);
        outputStream.writeByte(this.mouseClick);
        outputStream.writeShort(this.action);
        outputStream.writeBoolean(this.field_27039_f);
        if (this.itemStack == null) {
            outputStream.writeShort(-1);
        } else {
            outputStream.writeShort(this.itemStack.itemID);
            outputStream.writeByte(this.itemStack.stackSize);
            outputStream.writeShort(this.itemStack.getItemDamage());
        }

    }

    public int getPacketSize() {
        return 11;
    }
}
