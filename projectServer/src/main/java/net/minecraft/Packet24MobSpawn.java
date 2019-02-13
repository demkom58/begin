package net.minecraft;

import util.MathHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class Packet24MobSpawn extends Packet {
    public int entityId;
    public byte type;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public byte yaw;
    public byte pitch;
    private DataWatcher metaData;
    private List<WatchableObject> receivedMetadata;

    public Packet24MobSpawn() {
    }

    public Packet24MobSpawn(EntityLiving var1) {
        this.entityId = var1.entityId;
        this.type = (byte) EntityList.getEntityID(var1);
        this.xPosition = MathHelper.floor_double(var1.posX * 32.0D);
        this.yPosition = MathHelper.floor_double(var1.posY * 32.0D);
        this.zPosition = MathHelper.floor_double(var1.posZ * 32.0D);
        this.yaw = (byte) ((int) (var1.rotationYaw * 256.0F / 360.0F));
        this.pitch = (byte) ((int) (var1.rotationPitch * 256.0F / 360.0F));
        this.metaData = var1.getDataWatcher();
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.entityId = inputStream.readInt();
        this.type = inputStream.readByte();
        this.xPosition = inputStream.readInt();
        this.yPosition = inputStream.readInt();
        this.zPosition = inputStream.readInt();
        this.yaw = inputStream.readByte();
        this.pitch = inputStream.readByte();
        this.receivedMetadata = DataWatcher.readWatchableObjects(inputStream);
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.entityId);
        outputStream.writeByte(this.type);
        outputStream.writeInt(this.xPosition);
        outputStream.writeInt(this.yPosition);
        outputStream.writeInt(this.zPosition);
        outputStream.writeByte(this.yaw);
        outputStream.writeByte(this.pitch);
        this.metaData.writeWatchableObjects(outputStream);
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handleMobSpawn(this);
    }

    public int getPacketSize() {
        return 20;
    }
}
