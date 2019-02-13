package net.minecraft;

import util.MathHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet23VehicleSpawn extends Packet {
    public int entityId;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int field_28044_e;
    public int field_28043_f;
    public int field_28042_g;
    public int type;
    public int field_28041_i;

    public Packet23VehicleSpawn() {
    }

    public Packet23VehicleSpawn(Entity var1, int var2) {
        this(var1, var2, 0);
    }

    public Packet23VehicleSpawn(Entity var1, int var2, int var3) {
        this.entityId = var1.entityId;
        this.xPosition = MathHelper.floor_double(var1.posX * 32.0D);
        this.yPosition = MathHelper.floor_double(var1.posY * 32.0D);
        this.zPosition = MathHelper.floor_double(var1.posZ * 32.0D);
        this.type = var2;
        this.field_28041_i = var3;
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

            this.field_28044_e = (int) (var4 * 8000.0D);
            this.field_28043_f = (int) (var6 * 8000.0D);
            this.field_28042_g = (int) (var8 * 8000.0D);
        }

    }

    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.entityId = inputStream.readInt();
        this.type = inputStream.readByte();
        this.xPosition = inputStream.readInt();
        this.yPosition = inputStream.readInt();
        this.zPosition = inputStream.readInt();
        this.field_28041_i = inputStream.readInt();
        if (this.field_28041_i > 0) {
            this.field_28044_e = inputStream.readShort();
            this.field_28043_f = inputStream.readShort();
            this.field_28042_g = inputStream.readShort();
        }

    }

    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.entityId);
        outputStream.writeByte(this.type);
        outputStream.writeInt(this.xPosition);
        outputStream.writeInt(this.yPosition);
        outputStream.writeInt(this.zPosition);
        outputStream.writeInt(this.field_28041_i);
        if (this.field_28041_i > 0) {
            outputStream.writeShort(this.field_28044_e);
            outputStream.writeShort(this.field_28043_f);
            outputStream.writeShort(this.field_28042_g);
        }

    }

    public void processPacket(NetHandler netHandler) {
        netHandler.handleVehicleSpawn(this);
    }

    public int getPacketSize() {
        return 21 + this.field_28041_i > 0 ? 6 : 0;
    }
}
