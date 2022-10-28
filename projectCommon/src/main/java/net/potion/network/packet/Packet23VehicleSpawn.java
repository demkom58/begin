package net.potion.network.packet;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.hypnosis.util.math.MathHelper;
import net.potion.entity.Entity;
import net.potion.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet23VehicleSpawn extends Packet {
    public int entityId;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int motionX;
    public int motionY;
    public int motionZ;
    public int type;
    public int ownerId;

    public Packet23VehicleSpawn() {
    }

    @Side(CodeSide.SERVER)
    public Packet23VehicleSpawn(Entity var1, int var2) {
        this(var1, var2, 0);
    }

    @Side(CodeSide.SERVER)
    public Packet23VehicleSpawn(Entity var1, int var2, int var3) {
        this.entityId = var1.entityId;
        this.xPosition = MathHelper.floor(var1.posX * 32.0D);
        this.yPosition = MathHelper.floor(var1.posY * 32.0D);
        this.zPosition = MathHelper.floor(var1.posZ * 32.0D);
        this.type = var2;
        this.ownerId = var3;
        if (var3 > 0) {
            double var4 = var1.motionX;
            double var6 = var1.motionY;
            double var8 = var1.motionZ;
            double var10 = 3.9D;
            if (var4 < -var10) {
                var4 = -var10;
            }

            if (var6 < -var10) {
                var6 = -var10;
            }

            if (var8 < -var10) {
                var8 = -var10;
            }

            if (var4 > var10) {
                var4 = var10;
            }

            if (var6 > var10) {
                var6 = var10;
            }

            if (var8 > var10) {
                var8 = var10;
            }

            this.motionX = (int) (var4 * 8000.0D);
            this.motionY = (int) (var6 * 8000.0D);
            this.motionZ = (int) (var8 * 8000.0D);
        }

    }

    @Override
    public void readPacketData(DataInputStream var1) throws IOException {
        this.entityId = var1.readInt();
        this.type = var1.readByte();
        this.xPosition = var1.readInt();
        this.yPosition = var1.readInt();
        this.zPosition = var1.readInt();
        this.ownerId = var1.readInt();
        if (this.ownerId > 0) {
            this.motionX = var1.readShort();
            this.motionY = var1.readShort();
            this.motionZ = var1.readShort();
        }

    }

    @Override
    public void writePacketData(DataOutputStream var1) throws IOException {
        var1.writeInt(this.entityId);
        var1.writeByte(this.type);
        var1.writeInt(this.xPosition);
        var1.writeInt(this.yPosition);
        var1.writeInt(this.zPosition);
        var1.writeInt(this.ownerId);
        if (this.ownerId > 0) {
            var1.writeShort(this.motionX);
            var1.writeShort(this.motionY);
            var1.writeShort(this.motionZ);
        }

    }

    @Override
    public void processPacket(NetHandler var1) {
        var1.handleVehicleSpawn(this);
    }

    @Override
    public int getPacketSize() {
        return 21 + this.ownerId > 0 ? 6 : 0;
    }
}
