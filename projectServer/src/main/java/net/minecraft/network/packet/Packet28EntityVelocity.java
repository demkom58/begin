package net.minecraft.network.packet;

import net.minecraft.entity.Entity;
import net.minecraft.network.NetHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet28EntityVelocity extends Packet {
    public int entityId;
    public int motionX;
    public int motionY;
    public int motionZ;

    public Packet28EntityVelocity() {
    }

    public Packet28EntityVelocity(Entity var1) {
        this(var1.entityId, var1.motionX, var1.motionY, var1.motionZ);
    }

    public Packet28EntityVelocity(int var1, double var2, double var4, double var6) {
        this.entityId = var1;
        double var8 = 3.9D;
        if (var2 < -var8) {
            var2 = -var8;
        }

        if (var4 < -var8) {
            var4 = -var8;
        }

        if (var6 < -var8) {
            var6 = -var8;
        }

        if (var2 > var8) {
            var2 = var8;
        }

        if (var4 > var8) {
            var4 = var8;
        }

        if (var6 > var8) {
            var6 = var8;
        }

        this.motionX = (int) (var2 * 8000.0D);
        this.motionY = (int) (var4 * 8000.0D);
        this.motionZ = (int) (var6 * 8000.0D);
    }

    @Override
    public void readPacketData(DataInputStream inputStream) throws IOException {
        this.entityId = inputStream.readInt();
        this.motionX = inputStream.readShort();
        this.motionY = inputStream.readShort();
        this.motionZ = inputStream.readShort();
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(this.entityId);
        outputStream.writeShort(this.motionX);
        outputStream.writeShort(this.motionY);
        outputStream.writeShort(this.motionZ);
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        netHandler.func_6002_a(this);
    }

    @Override
    public int getPacketSize() {
        return 10;
    }
}
