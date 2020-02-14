package net.potion.network.packet;

import net.potion.item.ItemStack;
import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet15Place extends Packet {
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int direction;
    public ItemStack itemStack;

    public Packet15Place() {
    }

    public Packet15Place(int x, int y, int z, int direction, ItemStack itemStack) {
        this.xPosition = x;
        this.yPosition = y;
        this.zPosition = z;
        this.direction = direction;
        this.itemStack = itemStack;
    }

    @Override
    public void readPacketData(DataInputStream dis) throws IOException {
        this.xPosition = dis.readInt();
        this.yPosition = dis.read();
        this.zPosition = dis.readInt();
        this.direction = dis.read();

        short id = dis.readShort();
        if (id >= 0) {
            byte size = dis.readByte();
            short damage = dis.readShort();
            this.itemStack = new ItemStack(id, size, damage);
        } else {
            this.itemStack = null;
        }

    }

    @Override
    public void writePacketData(DataOutputStream dos) throws IOException {
        dos.writeInt(this.xPosition);
        dos.write(this.yPosition);
        dos.writeInt(this.zPosition);
        dos.write(this.direction);

        if (this.itemStack == null) {
            dos.writeShort(-1);
        } else {
            dos.writeShort(this.itemStack.itemID);
            dos.writeByte(this.itemStack.stackSize);
            dos.writeShort(this.itemStack.getItemDamage());
        }

    }

    @Override
    public void processPacket(NetHandler handler) {
        handler.handlePlace(this);
    }

    @Override
    public int getPacketSize() {
        return 15;
    }
}
