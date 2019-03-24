package net.minecraft.network.packet;

import net.minecraft.entity.Entity;
import net.minecraft.network.NetHandler;
import util.MathHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet34EntityTeleport extends Packet {
    public int entityId;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public byte yaw;
    public byte pitch;

    public Packet34EntityTeleport() {
    }

    public Packet34EntityTeleport(Entity var1) {
        this.entityId = var1.entityId;
        this.xPosition = MathHelper.floor(var1.posX * 32.0D);
        this.yPosition = MathHelper.floor(var1.posY * 32.0D);
        this.zPosition = MathHelper.floor(var1.posZ * 32.0D);
        this.yaw = (byte) ((int) (var1.rotationYaw * 256.0F / 360.0F));
        this.pitch = (byte) ((int) (var1.rotationPitch * 256.0F / 360.0F));
    }

    public Packet34EntityTeleport(int var1, int var2, int var3, int var4, byte var5, byte var6) {
        this.entityId = var1;
        this.xPosition = var2;
        this.yPosition = var3;
        this.zPosition = var4;
        this.yaw = var5;
        this.pitch = var6;
    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.entityId = inputStream.readInt();
        this.xPosition = inputStream.readInt();
        this.yPosition = inputStream.readInt();
        this.zPosition = inputStream.readInt();
        this.yaw = (byte) inputStream.read();
        this.pitch = (byte) inputStream.read();
    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.entityId);
        outputStream.writeInt(this.xPosition);
        outputStream.writeInt(this.yPosition);
        outputStream.writeInt(this.zPosition);
        outputStream.write(this.yaw);
        outputStream.write(this.pitch);
    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handleEntityTeleport(this);
    }

    public int getPacketSize() {
        return 34;
    }
}
