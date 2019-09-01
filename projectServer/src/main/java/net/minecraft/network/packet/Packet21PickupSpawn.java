package net.minecraft.network.packet;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.NetHandler;
import net.minecraft.util.MathHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet21PickupSpawn extends Packet {
    public int entityId;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public byte rotation;
    public byte pitch;
    public byte roll;
    public int itemID;
    public int count;
    public int itemDamage;

    public Packet21PickupSpawn() {
    }

    public Packet21PickupSpawn(EntityItem var1) {
        this.entityId = var1.entityId;
        this.itemID = var1.item.itemID;
        this.count = var1.item.stackSize;
        this.itemDamage = var1.item.getItemDamage();
        this.xPosition = MathHelper.floor(var1.posX * 32.0D);
        this.yPosition = MathHelper.floor(var1.posY * 32.0D);
        this.zPosition = MathHelper.floor(var1.posZ * 32.0D);
        this.rotation = (byte) ((int) (var1.motionX * 128.0D));
        this.pitch = (byte) ((int) (var1.motionY * 128.0D));
        this.roll = (byte) ((int) (var1.motionZ * 128.0D));
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.entityId = inputStream.readInt();
        this.itemID = inputStream.readShort();
        this.count = inputStream.readByte();
        this.itemDamage = inputStream.readShort();
        this.xPosition = inputStream.readInt();
        this.yPosition = inputStream.readInt();
        this.zPosition = inputStream.readInt();
        this.rotation = inputStream.readByte();
        this.pitch = inputStream.readByte();
        this.roll = inputStream.readByte();
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.entityId);
        outputStream.writeShort(this.itemID);
        outputStream.writeByte(this.count);
        outputStream.writeShort(this.itemDamage);
        outputStream.writeInt(this.xPosition);
        outputStream.writeInt(this.yPosition);
        outputStream.writeInt(this.zPosition);
        outputStream.writeByte(this.rotation);
        outputStream.writeByte(this.pitch);
        outputStream.writeByte(this.roll);
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handlePickupSpawn(this);
    }

    public int getPacketSize() {
        return 24;
    }
}
