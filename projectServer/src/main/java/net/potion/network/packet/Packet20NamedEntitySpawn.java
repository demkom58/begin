package net.potion.network.packet;

import net.potion.entity.player.EntityPlayer;
import net.potion.item.ItemStack;
import net.potion.network.NetHandler;
import net.hypnosis.util.math.MathHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet20NamedEntitySpawn extends Packet {
    public int entityId;
    public String name;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public byte rotation;
    public byte pitch;
    public int currentItem;

    public Packet20NamedEntitySpawn() {
    }

    public Packet20NamedEntitySpawn(EntityPlayer var1) {
        this.entityId = var1.entityId;
        this.name = var1.username;
        this.xPosition = MathHelper.floor(var1.posX * 32.0D);
        this.yPosition = MathHelper.floor(var1.posY * 32.0D);
        this.zPosition = MathHelper.floor(var1.posZ * 32.0D);
        this.rotation = (byte) ((int) (var1.rotationYaw * 256.0F / 360.0F));
        this.pitch = (byte) ((int) (var1.rotationPitch * 256.0F / 360.0F));
        ItemStack var2 = var1.inventory.getCurrentItem();
        this.currentItem = var2 == null ? 0 : var2.itemID;
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.entityId = inputStream.readInt();
        this.name = readString(inputStream, 16);
        this.xPosition = inputStream.readInt();
        this.yPosition = inputStream.readInt();
        this.zPosition = inputStream.readInt();
        this.rotation = inputStream.readByte();
        this.pitch = inputStream.readByte();
        this.currentItem = inputStream.readShort();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.entityId);
        writeString(this.name, outputStream);
        outputStream.writeInt(this.xPosition);
        outputStream.writeInt(this.yPosition);
        outputStream.writeInt(this.zPosition);
        outputStream.writeByte(this.rotation);
        outputStream.writeByte(this.pitch);
        outputStream.writeShort(this.currentItem);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.handleNamedEntitySpawn(this);
    }

    @Override
    public int getPacketSize() {
        return 28;
    }
}
