package net.minecraft;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet15Place extends Packet {
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int direction;
    public ItemStack itemStack;

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.xPosition = inputStream.readInt();
        this.yPosition = inputStream.read();
        this.zPosition = inputStream.readInt();
        this.direction = inputStream.read();
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
        outputStream.writeInt(this.xPosition);
        outputStream.write(this.yPosition);
        outputStream.writeInt(this.zPosition);
        outputStream.write(this.direction);
        if (this.itemStack == null) {
            outputStream.writeShort(-1);
        } else {
            outputStream.writeShort(this.itemStack.itemID);
            outputStream.writeByte(this.itemStack.stackSize);
            outputStream.writeShort(this.itemStack.getItemDamage());
        }

    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handlePlace(this);
    }

    public int getPacketSize() {
        return 15;
    }
}
